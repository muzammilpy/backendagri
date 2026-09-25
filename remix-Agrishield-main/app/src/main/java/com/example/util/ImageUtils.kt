package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.math.max

object ImageUtils {

    /**
     * Converts a Uri to a scaled down Bitmap to prevent OutOfMemory errors and optimize network transmission.
     */
    fun uriToBitmap(context: Context, uri: Uri, maxDimension: Int = 1024): Bitmap? {
        return try {
            val contentResolver = context.contentResolver

            // 1. Decode bounds to calculate sample size
            var inputStream: InputStream? = contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            val origWidth = options.outWidth
            val origHeight = options.outHeight
            if (origWidth <= 0 || origHeight <= 0) return null

            var inSampleSize = 1
            val maxOriginal = max(origWidth, origHeight)
            while (maxOriginal / inSampleSize > maxDimension) {
                inSampleSize *= 2
            }

            // 2. Decode actual bitmap with sample size
            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, decodeOptions)
            inputStream?.close()

            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Encodes a Bitmap to a Base64 string in JPEG format.
     */
    fun Bitmap.toBase64(quality: Int = 80): String {
        val outputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    /**
     * Generates a representative synthetic specimen leaf bitmap for presets.
     * Allows preset samples to also be evaluated as multimodal images if needed.
     */
    fun createSpecimenBitmap(sampleKey: String, width: Int = 512, height: Int = 512): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Background
        val bgColor = when (sampleKey) {
            "tomato_late_blight" -> android.graphics.Color.rgb(139, 30, 30)
            "rice_blast" -> android.graphics.Color.rgb(46, 111, 64)
            "maize_fall_armyworm" -> android.graphics.Color.rgb(153, 88, 42)
            "cotton_pink_bollworm" -> android.graphics.Color.rgb(107, 66, 38)
            "wheat_rust" -> android.graphics.Color.rgb(176, 125, 30)
            else -> android.graphics.Color.rgb(56, 107, 29)
        }
        canvas.drawColor(bgColor)

        // Draw leaf silhouette
        paint.color = android.graphics.Color.rgb(90, 150, 60)
        paint.style = Paint.Style.FILL
        val leafRect = RectF(60f, 60f, width - 60f, height - 60f)
        canvas.drawOval(leafRect, paint)

        // Draw disease spots / lesions
        paint.color = when (sampleKey) {
            "tomato_late_blight" -> android.graphics.Color.rgb(50, 20, 20)
            "rice_blast" -> android.graphics.Color.rgb(180, 180, 180)
            "maize_fall_armyworm" -> android.graphics.Color.rgb(80, 40, 10)
            "wheat_rust" -> android.graphics.Color.rgb(255, 200, 0)
            else -> android.graphics.Color.rgb(100, 40, 40)
        }
        canvas.drawCircle(width * 0.4f, height * 0.4f, 45f, paint)
        canvas.drawCircle(width * 0.65f, height * 0.55f, 60f, paint)
        canvas.drawCircle(width * 0.45f, height * 0.7f, 35f, paint)

        return bitmap
    }
}
