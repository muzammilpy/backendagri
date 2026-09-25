package com.agrishield.app.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.ImageProxy
import com.agrishield.app.data.model.Prediction
import com.agrishield.app.data.model.Severity
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * CottonClassifier
 * ================
 * Loads agrishield_cotton.tflite from assets/, runs inference on a Bitmap,
 * returns real softmax probabilities per class.
 *
 * The model was trained with MobileNetV2 on the Kaggle Cotton Disease dataset.
 * Input:  [1, 224, 224, 3]  float32, values 0.0–1.0
 * Output: [1, NUM_CLASSES]  float32, softmax probabilities
 */
class CottonClassifier(private val context: Context) {

    companion object {
        private const val TAG           = "CottonClassifier"
        private const val MODEL_FILE    = "agrishield_cotton.tflite"
        private const val LABELS_FILE   = "labels.txt"
        private const val METADATA_FILE = "model_metadata.json"
        private const val IMG_SIZE      = 224
        private const val NUM_THREADS   = 4
    }

    private var interpreter: Interpreter? = null
    private lateinit var labels: List<String>
    private lateinit var metadata: JsonObject

    // ── Initialise ──────────────────────────────────────────────────────

    fun initialize() {
        try {
            labels   = loadLabels()
            metadata = loadMetadata()

            val options = Interpreter.Options().apply {
                numThreads = NUM_THREADS
                // CPU-only inference — GPU delegate removed due to
                // GpuDelegateFactory$Options compatibility issue on TFLite 2.14
                // CPU gives ~300ms inference which is perfectly acceptable
            }
            interpreter = Interpreter(loadModelFile(), options)
            Log.i(TAG, "Model loaded (CPU, $NUM_THREADS threads). Classes: ${labels.size}")
        } catch (e: IOException) {
            Log.e(TAG, "Failed to load model: ${e.message}")
            throw RuntimeException(
                "Could not load $MODEL_FILE from assets. " +
                "Did you run train_model.py and copy the .tflite file?", e
            )
        }
    }

    fun isReady(): Boolean = interpreter != null

    // ── Inference ───────────────────────────────────────────────────────

    /**
     * Classify a Bitmap.
     * Returns list of Prediction sorted by confidence descending.
     * confidence is a REAL softmax probability 0–100, not a hardcoded number.
     */
    fun classify(bitmap: Bitmap): List<Prediction> {
        val interp = interpreter ?: throw IllegalStateException("Classifier not initialized")

        val t0 = System.currentTimeMillis()
        val input  = preprocessBitmap(bitmap)
        val output = Array(1) { FloatArray(labels.size) }

        interp.run(input, output)

        val elapsedMs = System.currentTimeMillis() - t0
        val probs = output[0]   // raw softmax from the model

        return probs.mapIndexed { idx, prob ->
            val label    = labels.getOrElse(idx) { "class_$idx" }
            val classObj = metadata
                .getAsJsonObject("classes")
                ?.getAsJsonObject(idx.toString())

            Prediction(
                label          = label,
                confidence     = prob * 100f,       // real softmax, NOT hardcoded
                rank           = 0,                 // filled below
                displayName    = classObj?.get("display_name")?.asString ?: label,
                severity       = parseSeverity(classObj?.get("severity")?.asString),
                immediateAction= classObj?.get("immediate_action")?.asString
                                  ?: "Consult local KVK agronomist.",
                likelyCauses   = classObj?.getAsJsonArray("likely_causes")
                                  ?.map { it.asString } ?: emptyList(),
                chemical       = classObj?.get("chemical")?.asString ?: "",
                biological     = classObj?.get("biological")?.asString ?: "",
                marathi        = classObj?.get("marathi")?.asString ?: label,
                hindi          = classObj?.get("hindi")?.asString ?: label,
                inferenceMseconds = elapsedMs
            )
        }
            .sortedByDescending { it.confidence }
            .mapIndexed { i, p -> p.copy(rank = i + 1) }
    }

    /**
     * Classify from a CameraX ImageProxy (live camera feed).
     */
    fun classifyFromImageProxy(imageProxy: ImageProxy): List<Prediction> {
        val bitmap = imageProxyToBitmap(imageProxy)
        return classify(bitmap)
    }

    // ── Preprocessing ───────────────────────────────────────────────────

    private fun preprocessBitmap(src: Bitmap): ByteBuffer {
        // Scale to 224x224
        val scaled = Bitmap.createScaledBitmap(src, IMG_SIZE, IMG_SIZE, true)

        val buf = ByteBuffer.allocateDirect(4 * IMG_SIZE * IMG_SIZE * 3)
        buf.order(ByteOrder.nativeOrder())

        val pixels = IntArray(IMG_SIZE * IMG_SIZE)
        scaled.getPixels(pixels, 0, IMG_SIZE, 0, 0, IMG_SIZE, IMG_SIZE)

        for (px in pixels) {
            // Normalise each channel to [0, 1] — matches training preprocessing
            buf.putFloat(((px shr 16) and 0xFF) / 255.0f)   // R
            buf.putFloat(((px shr 8)  and 0xFF) / 255.0f)   // G
            buf.putFloat(( px         and 0xFF) / 255.0f)   // B
        }
        buf.rewind()
        return buf
    }

    private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
        val yBuffer = imageProxy.planes[0].buffer
        val uBuffer = imageProxy.planes[1].buffer
        val vBuffer = imageProxy.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21,
            imageProxy.width, imageProxy.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(0, 0, imageProxy.width, imageProxy.height), 90, out
        )
        val bytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    // ── Asset loading ────────────────────────────────────────────────────

    private fun loadModelFile(): MappedByteBuffer {
        val afd = context.assets.openFd(MODEL_FILE)
        val stream = FileInputStream(afd.fileDescriptor)
        val channel = stream.channel
        return channel.map(FileChannel.MapMode.READ_ONLY, afd.startOffset, afd.declaredLength)
    }

    private fun loadLabels(): List<String> =
        context.assets.open(LABELS_FILE)
            .bufferedReader()
            .readLines()
            .filter { it.isNotBlank() }

    private fun loadMetadata(): JsonObject {
        val json = context.assets.open(METADATA_FILE)
            .bufferedReader().readText()
        return Gson().fromJson(json, JsonObject::class.java)
    }

    private fun parseSeverity(s: String?): Severity = when (s?.uppercase()) {
        "CRITICAL" -> Severity.CRITICAL
        "HIGH"     -> Severity.HIGH
        "MODERATE" -> Severity.MODERATE
        else       -> Severity.LOW
    }

    // ── Cleanup ──────────────────────────────────────────────────────────

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
