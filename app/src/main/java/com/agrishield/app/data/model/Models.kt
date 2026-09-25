package com.agrishield.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// ── ML Prediction result ─────────────────────────────────────────────────

data class Prediction(
    val label: String,
    val confidence: Float,       // 0.0 – 100.0 (real softmax * 100)
    val rank: Int,
    val displayName: String,
    val severity: Severity,
    val immediateAction: String,
    val likelyCauses: List<String>,
    val chemical: String,
    val biological: String,
    val marathi: String,
    val hindi: String,
    val inferenceMseconds: Long
)

enum class Severity { LOW, MODERATE, HIGH, CRITICAL }

data class DiagnosisResult(
    val topPrediction: Prediction,
    val allPredictions: List<Prediction>,
    val confidenceBand: String,
    val farmLat: Double,
    val farmLon: Double,
    val language: String,
    val modelSource: String = "AgriShield TFLite v1.0 (MobileNetV2 / Cotton)",
    val weatherRisk: WeatherRisk? = null
)

// ── Weather data ─────────────────────────────────────────────────────────

data class WeatherRisk(
    val tempMax: Float,
    val tempMin: Float,
    val humidityMax: Int,
    val rainMm: Float,
    val rainProbPct: Int,
    val windKmh: Float,
    val condition: String,
    val fungalRisk: Severity,
    val pestRisk: Severity,
    val advisory: String,
    val advisoryMr: String = "",
    val advisoryHi: String = ""
)

data class WeatherForecast(
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val days: List<WeatherRisk>
)

// ── Room entity: saved diagnosis ─────────────────────────────────────────

@Entity(tableName = "diagnoses")
data class DiagnosisEntity(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val farmLat: Double,
    val farmLon: Double,
    val districtName: String,
    val imagePath: String,
    val cropLabel: String,
    val diagnosisLabel: String,
    val confidence: Float,
    val severity: String,
    val immediateAction: String,
    val chemical: String,
    val biological: String,
    val isSyncedToServer: Boolean = false,
    val serverReportId: String? = null,
    val feedbackOutcome: String? = null,   // SUCCESS / PARTIAL / FAILED
    val feedbackNotes: String? = null,
    val expertNotes: String? = null
)

// ── Maharashtra district GIS record ──────────────────────────────────────

data class DistrictHotspot(
    val name: String,
    val division: String,
    val lat: Double,
    val lon: Double,
    val majorCrops: List<String>,
    val soilType: String,
    val primaryThreat: String,
    val outbreakRisk: Severity,
    val trapCount: Int,
    val ndvi: Double,
    val isStressed: Boolean,
    val advisory: String,
    val advisoryMr: String = ""
)

// ── Open-Meteo API response models ───────────────────────────────────────

data class OpenMeteoResponse(
    @SerializedName("daily") val daily: DailyData?
)

data class DailyData(
    @SerializedName("time")                         val time: List<String>?,
    @SerializedName("temperature_2m_max")           val tempMax: List<Float?>?,
    @SerializedName("temperature_2m_min")           val tempMin: List<Float?>?,
    @SerializedName("relative_humidity_2m_max")     val humidityMax: List<Int?>?,
    @SerializedName("relative_humidity_2m_min")     val humidityMin: List<Int?>?,
    @SerializedName("precipitation_sum")            val rainMm: List<Float?>?,
    @SerializedName("precipitation_probability_max")val rainProb: List<Int?>?,
    @SerializedName("wind_speed_10m_max")           val windMax: List<Float?>?,
    @SerializedName("weather_code")                 val weatherCode: List<Int?>?
)

// ── Server sync payload ───────────────────────────────────────────────────

data class SyncPayload(
    val farmerId: String,
    val farmLat: Double,
    val farmLon: Double,
    val district: String,
    val crop: String,
    val diagnosis: String,
    val confidence: Float,
    val severity: String,
    val symptoms: String,
    val imageBase64: String?
)

data class SyncResponse(
    val success: Boolean,
    val reportId: String?,
    val error: String?
)

// ── App language ─────────────────────────────────────────────────────────

enum class AppLanguage(val code: String, val displayName: String, val ttsLocale: String) {
    ENGLISH("en", "English", "en-IN"),
    MARATHI("mr", "मराठी", "mr-IN"),
    HINDI("hi", "हिंदी", "hi-IN"),
    GUJARATI("gu", "ગુજ", "gu-IN")
}
