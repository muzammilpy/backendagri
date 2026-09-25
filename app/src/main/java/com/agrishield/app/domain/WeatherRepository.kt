package com.agrishield.app.domain

import android.util.Log
import com.agrishield.app.data.model.*
import com.agrishield.app.data.remote.WeatherApiClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeatherRepository {

    suspend fun getForecast(lat: Double, lon: Double): Result<WeatherForecast> {
        return try {
            val resp = WeatherApiClient.service.getForecast(lat, lon)
            val daily = resp.daily ?: return Result.failure(Exception("No daily data"))
            val days = buildWeatherRisks(daily)
            Result.success(
                WeatherForecast(
                    locationName = "Your Farm Location",
                    latitude = lat,
                    longitude = lon,
                    days = days
                )
            )
        } catch (e: Exception) {
            Log.w("WeatherRepo", "API failed, using fallback: ${e.message}")
            Result.success(offlineFallback(lat, lon))
        }
    }

    private fun buildWeatherRisks(daily: com.agrishield.app.data.model.DailyData): List<WeatherRisk> {
        val dates = daily.time ?: return emptyList()
        return dates.indices.map { i ->
            val tMax   = daily.tempMax?.getOrNull(i)    ?: 31f
            val tMin   = daily.tempMin?.getOrNull(i)    ?: 22f
            val hMax   = daily.humidityMax?.getOrNull(i)?: 72
            val hMin   = daily.humidityMin?.getOrNull(i)?: 48
            val rain   = daily.rainMm?.getOrNull(i)     ?: 0f
            val rProb  = daily.rainProb?.getOrNull(i)   ?: 15
            val wind   = daily.windMax?.getOrNull(i)    ?: 12f
            val code   = daily.weatherCode?.getOrNull(i)?: 0
            val cond   = weatherCodeToString(code)

            val fungal = computeFungalRisk(hMax, hMin, tMax, tMin, rProb, rain)
            val pest   = computePestRisk(tMax, tMin, hMin, wind)

            WeatherRisk(
                tempMax      = tMax,
                tempMin      = tMin,
                humidityMax  = hMax,
                rainMm       = rain,
                rainProbPct  = rProb,
                windKmh      = wind,
                condition    = cond,
                fungalRisk   = fungal,
                pestRisk     = pest,
                advisory     = buildAdvisory(fungal, pest, hMax, rain, rProb, tMax, wind, cond, "en"),
                advisoryMr   = buildAdvisory(fungal, pest, hMax, rain, rProb, tMax, wind, cond, "mr"),
                advisoryHi   = buildAdvisory(fungal, pest, hMax, rain, rProb, tMax, wind, cond, "hi"),
            )
        }
    }

    private fun computeFungalRisk(hMax: Int, hMin: Int, tMax: Float, tMin: Float,
                                   rainProb: Int, rainMm: Float): Severity {
        val avgH = (hMax + hMin) / 2.0
        val tempOk = (tMax in 16f..28f) || (tMin in 14f..24f)
        return when {
            (hMax >= 82 || avgH >= 80) && (rainProb >= 50 || rainMm >= 5f) && tempOk -> Severity.CRITICAL
            (hMax >= 78 || avgH >= 75) || (rainProb >= 40 && tempOk)                  -> Severity.HIGH
            hMax >= 62 || avgH >= 58 || rainProb >= 20                                -> Severity.MODERATE
            else                                                                       -> Severity.LOW
        }
    }

    private fun computePestRisk(tMax: Float, tMin: Float, hMin: Int, wind: Float): Severity {
        return when {
            tMax >= 34f && hMin <= 50               -> Severity.CRITICAL
            tMax >= 31f || (tMax >= 27f && wind in 8f..24f) -> Severity.HIGH
            tMax >= 22f                             -> Severity.MODERATE
            else                                    -> Severity.LOW
        }
    }

    private fun buildAdvisory(fungal: Severity, pest: Severity, hMax: Int, rain: Float,
                               rProb: Int, tMax: Float, wind: Float, cond: String,
                               lang: String): String {
        val templates = mapOf(
            "critical_fungal" to mapOf(
                "en" to "🚨 CRITICAL Spore Alert (RH $hMax%, Rain ${rain}mm). Apply Copper Oxychloride 50 WP @ 2.5g/L before rain. Avoid overhead irrigation.",
                "mr" to "🚨 गंभीर बुरशी इशारा (आर्द्रता $hMax%, पाऊस ${rain}mm). पावसापूर्वी कॉपर ऑक्सिक्लोराइड 50 WP @ 2.5g/L फवारणी करा.",
                "hi" to "🚨 गंभीर फफूंद चेतावनी (नमी $hMax%, बारिश ${rain}mm). बारिश से पहले कॉपर ऑक्सिक्लोराइड @ 2.5g/L छिड़कें."
            ),
            "critical_pest" to mapOf(
                "en" to "🚨 CRITICAL Pest Vector Alert (Temp ${tMax}°C, Low RH). Whitefly/thrips surge. Install 20 yellow sticky traps/acre. Spray NSKE 5%.",
                "mr" to "🚨 गंभीर कीड धोका (तापमान ${tMax}°C). पांढरी माशी वाढ. पिवळे चिकट सापळे लावा. निंबोळी अर्क 5% फवारणी.",
                "hi" to "🚨 गंभीर कीट खतरा (तापमान ${tMax}°C). सफेद मक्खी वृद्धि। पीले चिपचिपे कार्ड लगाएं।"
            ),
            "high_fungal" to mapOf(
                "en" to "⚠️ HIGH Blight Risk (RH $hMax%, Rain prob $rProb%). Drain fields. Apply Mancozeb 75 WP @ 2.0g/L.",
                "mr" to "⚠️ उच्च करपा धोका (आर्द्रता $hMax%). शेत निचरा करा. Mancozeb 75 WP @ 2.0g/L फवारा.",
                "hi" to "⚠️ उच्च झुलसा खतरा (नमी $hMax%). जल निकासी करें। Mancozeb 75 WP @ 2.0g/L छिड़कें।"
            ),
            "moderate" to mapOf(
                "en" to "ℹ️ MODERATE Risk ($cond). Scout 20 plants/acre. Use drip irrigation only.",
                "mr" to "ℹ️ मध्यम धोका ($cond). 20 झाडांची पाहणी करा. ठिबक सिंचन वापरा.",
                "hi" to "ℹ️ मध्यम खतरा ($cond). 20 पौधों की निगरानी करें।"
            ),
            "low" to mapOf(
                "en" to "✅ LOW Risk — Favorable conditions ($cond, ${tMax.toInt()}°C). Optimal for fertiliser/weeding.",
                "mr" to "✅ कमी धोका — अनुकूल स्थिती ($cond, ${tMax.toInt()}°C). खत देण्यासाठी/खुरपणीसाठी योग्य.",
                "hi" to "✅ कम खतरा — अनुकूल ($cond, ${tMax.toInt()}°C). उर्वरक/निराई के लिए उपयुक्त।"
            )
        )
        val key = when {
            fungal == Severity.CRITICAL -> "critical_fungal"
            pest   == Severity.CRITICAL -> "critical_pest"
            fungal == Severity.HIGH     -> "high_fungal"
            fungal == Severity.MODERATE || pest == Severity.MODERATE -> "moderate"
            else                        -> "low"
        }
        return templates[key]?.get(lang) ?: templates[key]?.get("en") ?: ""
    }

    private fun weatherCodeToString(code: Int): String = when (code) {
        0        -> "Clear Sky"
        1, 2     -> "Partly Cloudy"
        3        -> "Overcast"
        45, 48   -> "Fog / Dew"
        51,53,55 -> "Drizzle"
        61,63,65 -> "Rain Showers"
        80,81,82 -> "Heavy Rain"
        95,96,99 -> "Thunderstorm"
        else     -> "Scattered Clouds"
    }

    private fun offlineFallback(lat: Double, lon: Double) = WeatherForecast(
        locationName = "Offline Forecast",
        latitude = lat, longitude = lon,
        days = listOf(
            WeatherRisk(32f, 22f, 78, 1.2f, 25, 14.5f, "Partly Cloudy",
                Severity.MODERATE, Severity.HIGH,
                "⚠️ HIGH Pest risk. Install pheromone traps. Scout daily.",
                "⚠️ उच्च कीड धोका. फेरोमोन सापळे लावा.",
                "⚠️ उच्च कीट खतरा।"),
            WeatherRisk(34f, 23f, 82, 8.4f, 65, 18f, "Rain Showers",
                Severity.CRITICAL, Severity.MODERATE,
                "🚨 CRITICAL Fungal risk. Apply Copper Oxychloride before rain.",
                "🚨 गंभीर बुरशी धोका. पावसापूर्वी कॉपर फवारणी करा.",
                "🚨 गंभीर फफूंद खतरा।"),
            WeatherRisk(31f, 21f, 88, 18.5f, 80, 22f, "Heavy Rain",
                Severity.CRITICAL, Severity.LOW,
                "🚨 CRITICAL Spore Alert. Do not spray. Drain fields immediately.",
                "🚨 गंभीर बीजाणू इशारा. फवारणी नको. शेत निचरा करा.",
                "🚨 गंभीर बीजाणु चेतावनी।")
        )
    )
}
