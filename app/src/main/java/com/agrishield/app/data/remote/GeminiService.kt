package com.agrishield.app.data.remote

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.agrishield.app.BuildConfig
import com.agrishield.app.data.model.Severity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

data class GeminiDiagnosis(
    val isValidPlant: Boolean = true,
    val rejectionReason: String = "",
    val cropName: String = "Cotton",
    val diseaseName: String,
    val scientificName: String,
    val severity: Severity,
    val confidence: Int,
    val immediateAction: String,
    val biologicalControl: String,
    val chemicalControl: String,
    val preventionTips: List<String>,
    val source: String = "Gemini 2.0 Flash Vision"
)

object GeminiService {

    private const val TAG = "GeminiService"
    private val API_KEY get() = BuildConfig.GEMINI_API_KEY
    private const val URL_TEMPLATE =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=%s"

    /**
     * Sends the bitmap to Gemini Vision API and returns a specific disease diagnosis.
     * Validates if the image actually contains a plant/crop leaf first.
     * Auto-detects the true crop species instead of assuming Cotton.
     */
    suspend fun diagnose(
        bitmap: Bitmap,
        cropName: String = "Auto-Detect",
        tfliteResult: String = "",
        lang: String = "en"
    ): GeminiDiagnosis? = withContext(Dispatchers.IO) {

        if (API_KEY == "YOUR_GEMINI_API_KEY" || API_KEY.isBlank()) {
            Log.w(TAG, "No Gemini API key configured")
            return@withContext null
        }

        try {
            val base64Image = bitmapToBase64(bitmap)

            val langInstruction = when (lang) {
                "mr" -> "Respond with diagnosis name and immediateAction in Marathi. Other fields in English."
                "hi" -> "Respond with diagnosis name and immediateAction in Hindi. Other fields in English."
                else -> "Respond entirely in English."
            }

            val isAutoDetect = cropName.isBlank() || cropName.equals("Auto-Detect", ignoreCase = true)

            val prompt = """
                You are an expert agricultural plant pathologist for Maharashtra, India.
                Analyze this image.
                Pre-screening info: ${if (isAutoDetect) "Auto-Detect crop from image" else cropName} (TFLite pre-screening: $tfliteResult)
                $langInstruction

                STEP 1: Determine if the image contains any plant, leaf, crop, fruit, stem, field vegetation, agricultural pest, or plant lesion.
                Only reject the image if it is unequivocally non-agricultural/non-botanical (e.g. human faces, furniture, vehicles, indoor household electronics, paper documents, architecture diagrams, wall background).

                If it is NOT a valid plant/crop image:
                Set "isValidPlant": false and "rejectionReason": "⚠️ Diagnosis Failed: No plant or crop leaf detected. Please take a clear photo of an infected leaf or field specimen."

                If it IS a valid plant/crop image:
                Set "isValidPlant": true
                Auto-detect the exact crop species (e.g. "Cotton", "Tomato", "Rice", "Wheat", "Maize", "Grape", "Potato", "Soybean", "Chilli", etc.).
                Identify the SPECIFIC disease or pest (or "Healthy" if no infection).

                Respond ONLY in this exact JSON format:
                {
                  "isValidPlant": true,
                  "rejectionReason": "",
                  "cropName": "detected crop species name",
                  "diseaseName": "specific disease or pest name or Healthy",
                  "scientificName": "scientific pathogen name or None",
                  "severity": "LOW|MODERATE|HIGH|CRITICAL",
                  "confidencePercentage": 85,
                  "immediateAction": "specific action in 1-2 sentences",
                  "biologicalControl": "specific biological treatment",
                  "chemicalControl": "specific chemical with dosage and PHI",
                  "preventionTips": ["tip1", "tip2", "tip3"]
                }
            """.trimIndent()

            val requestBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                            put(JSONObject().apply {
                                put("inlineData", JSONObject().apply {
                                    put("mimeType", "image/jpeg")
                                    put("data", base64Image)
                                })
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("responseMimeType", "application/json")
                })
            }

            val url = URL(URL_TEMPLATE.format(API_KEY))
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 15000
            conn.readTimeout = 30000

            conn.outputStream.use { it.write(requestBody.toString().toByteArray()) }

            val responseCode = conn.responseCode
            if (responseCode != 200) {
                Log.e(TAG, "Gemini API error: $responseCode")
                return@withContext null
            }

            val response = conn.inputStream.bufferedReader().readText()
            val json = JSONObject(response)
            val text = json
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

            val result = JSONObject(text)
            val isValidPlant = result.optBoolean("isValidPlant", result.optBoolean("is_valid_plant", true))
            val rejectionReason = result.optString("rejectionReason", result.optString("rejection_reason", ""))
            val detectedCrop = result.optString("cropName", result.optString("crop_name", "Cotton"))

            val sevStr = result.optString("severity", "MODERATE").uppercase()
            val sev = try { Severity.valueOf(sevStr) } catch (e: Exception) { Severity.MODERATE }

            val tips = mutableListOf<String>()
            result.optJSONArray("preventionTips")?.let { arr ->
                for (i in 0 until arr.length()) tips.add(arr.getString(i))
            }

            GeminiDiagnosis(
                isValidPlant    = isValidPlant,
                rejectionReason = rejectionReason,
                cropName        = if (detectedCrop.isNotBlank()) detectedCrop else "Cotton",
                diseaseName     = result.optString("diseaseName", result.optString("disease_name", "Unknown Disease")),
                scientificName  = result.optString("scientificName", result.optString("scientific_name", "Unknown")),
                severity        = sev,
                confidence      = result.optInt("confidencePercentage", result.optInt("confidence", 80)),
                immediateAction = result.optString("immediateAction", result.optString("immediate_action", "Consult local KVK.")),
                biologicalControl = result.optString("biologicalControl", result.optString("biological_control", "Apply NSKE 5%")),
                chemicalControl = result.optString("chemicalControl", result.optString("chemical_control", "Consult KVK for prescription")),
                preventionTips  = tips
            )

        } catch (e: Exception) {
            Log.e(TAG, "Gemini Vision call failed: ${e.message}")
            null
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val scaled = if (bitmap.width > 768) {
            val ratio = 768f / bitmap.width
            Bitmap.createScaledBitmap(
                bitmap,
                768,
                (bitmap.height * ratio).toInt(),
                true
            )
        } else bitmap

        val stream = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, 85, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }
}
