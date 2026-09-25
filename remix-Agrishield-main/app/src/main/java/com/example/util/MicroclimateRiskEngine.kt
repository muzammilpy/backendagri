package com.example.util

import com.example.model.ThreatSeverity
import com.example.model.WeatherRiskForecast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Agronomic risk engine that calculates dynamic fungal spore and pest vector threats
 * based on microclimate parameters (humidity, leaf wetness, temperature, wind, and rain).
 */
object MicroclimateRiskEngine {

    /**
     * Evaluates Fungal Spore Inoculation & Infection Risk.
     * High humidity (>80%), moderate canopy temperatures (17-26°C), and rain trigger
     * rapid conidial sporulation for Late Blight, Downy Mildew, and Leaf Blast.
     */
    fun computeFungalSporeRisk(
        humidityMax: Int,
        humidityMin: Int = humidityMax,
        tempMax: Int,
        tempMin: Int,
        rainProb: Int,
        rainMm: Float = 0f
    ): ThreatSeverity {
        val avgHumidity = (humidityMax + humidityMin) / 2
        val isSporeTempWindow = (tempMax in 16..28) || (tempMin in 14..24)

        return when {
            // Critical Risk: Extreme humidity (>82%) + rain/wetness in favorable spore temperature range
            (humidityMax >= 82 || avgHumidity >= 80) && (rainProb >= 50 || rainMm >= 5f) && isSporeTempWindow -> {
                ThreatSeverity.CRITICAL
            }
            // High: humid canopy plus rain/wetness in the spore temperature window
            (humidityMax >= 78 || avgHumidity >= 75) && isSporeTempWindow && (rainProb >= 35 || rainMm >= 2f) -> {
                ThreatSeverity.HIGH
            }
            // Moderate Risk: Moderate humidity (60-75%) or scattered light showers
            humidityMax >= 62 || avgHumidity >= 58 || rainProb >= 20 -> {
                ThreatSeverity.MODERATE
            }
            // Low Risk: Dry atmosphere, low rain probability
            else -> {
                ThreatSeverity.LOW
            }
        }
    }

    /**
     * Evaluates Pest Vector Migration & Proliferation Risk.
     * High temperatures (>30°C) accelerate egg-to-adult insect lifecycles (Whitefly, Thrips, Mites).
     * Moderate winds (8-22 km/h) facilitate flight dispersal and moth oviposition.
     */
    fun computePestVectorRisk(
        tempMax: Int,
        tempMin: Int = tempMax - 8,
        humidityMin: Int = 50,
        windSpeedKmh: Float = 10f
    ): ThreatSeverity {
        return when {
            // Critical Risk: Extreme heatwave (>34°C) with dry microclimate (rapid generation cycle)
            tempMax >= 34 && humidityMin <= 50 -> {
                ThreatSeverity.CRITICAL
            }
            // High Risk: Warm weather (>28°C) with active wind dispersal (8-24 km/h) or general heat > 32°C
            tempMax >= 31 || (tempMax >= 27 && windSpeedKmh in 8f..24f) -> {
                ThreatSeverity.HIGH
            }
            // Moderate Risk: Mild warm temperatures (23-28°C)
            tempMax >= 22 -> {
                ThreatSeverity.MODERATE
            }
            // Low Risk: Cool canopy temperatures (< 22°C) or heavy precipitation suppressing flight
            else -> {
                ThreatSeverity.LOW
            }
        }
    }

    /**
     * Generates actionable agronomic recommendations based on the day's calculated risks.
     */
    fun generateActionableAdvisory(
        dayIndex: Int,
        fungalRisk: ThreatSeverity,
        pestRisk: ThreatSeverity,
        tempMax: Int,
        tempMin: Int,
        humidityMax: Int,
        rainProb: Int,
        rainMm: Float,
        windSpeedKmh: Float,
        weatherCondition: String
    ): String {
        return when {
            fungalRisk == ThreatSeverity.CRITICAL -> {
                "⚠️ Critical Late Blight & Downy Mildew spore germination risk (RH ${humidityMax}%). Delay nitrogen top-dress; apply protective bio-fungicide (Trichoderma viride @ 5g/L) or Copper Oxychloride prior to rain."
            }
            rainMm >= 15f || (rainProb >= 75 && humidityMax >= 85) -> {
                "🌧️ High precipitation & prolonged leaf wetness forecast. Ensure drainage furrows and runoff trenches are cleared to prevent root hypoxia and water-soaked fungal lesions."
            }
            fungalRisk == ThreatSeverity.HIGH && pestRisk == ThreatSeverity.HIGH -> {
                "⚡ Post-rain warming period accelerates both fungal leaf spots and Whitefly/Armyworm oviposition. Inspect sticky traps and spray botanical neem 10,000 ppm during early morning."
            }
            pestRisk == ThreatSeverity.CRITICAL -> {
                "🔥 Heatwave threshold (${tempMax}°C) accelerates sucking pest lifecycles (egg-to-adult in 11 days). Install yellow sticky cards (15–20/acre) and apply Beauveria bassiana bio-insecticide."
            }
            pestRisk == ThreatSeverity.HIGH -> {
                "🪲 Moderate breeze (${windSpeedKmh.toInt()} km/h) and warm canopy favor pest vector dispersal. Check pheromone lures for Armyworm/Bollworm moths and prune lower senescent foliage."
            }
            rainProb <= 20 && windSpeedKmh <= 12f -> {
                "🌿 Optimal foliar application window (calm breeze ${windSpeedKmh.toInt()} km/h, $weatherCondition). Conduct preventive nutrient and bio-control spray between 6:30 AM and 8:30 AM."
            }
            fungalRisk == ThreatSeverity.MODERATE -> {
                "⛅ Moderate humidity (${humidityMax}%). Conduct routine leaf undersurface scouting for early powdery mildew conidia and maintain balanced drip irrigation."
            }
            else -> {
                "☀️ Favorable dry microclimate. Maintain regular irrigation schedule, record trap counts, and maintain standard agronomic field hygiene."
            }
        }
    }

    /**
     * Converts WMO weather codes (from Open-Meteo) into human-readable condition descriptions.
     */
    fun decodeWmoWeatherCode(code: Int): String {
        return when (code) {
            0 -> "Clear sky & sunshine"
            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast skies"
            45, 48 -> "Fog & morning mist"
            51, 53, 55 -> "Light drizzle"
            56, 57 -> "Freezing drizzle"
            61, 63 -> "Moderate rain showers"
            65 -> "Heavy continuous rain"
            66, 67 -> "Freezing rain"
            71, 73, 75 -> "Snowfall"
            77 -> "Snow grains"
            80, 81 -> "Scattered rain showers"
            82 -> "Violent rain showers"
            85, 86 -> "Snow showers"
            95 -> "Thunderstorm with rain"
            96, 99 -> "Thunderstorm with hail"
            else -> "Partly cloudy"
        }
    }

    /**
     * Formats real-time dynamic dates starting from today for N consecutive days.
     */
    fun generateDynamicDayLabels(daysCount: Int = 6): List<Pair<String, String>> {
        val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val result = mutableListOf<Pair<String, String>>()

        for (i in 0 until daysCount) {
            val dateText = dateFormat.format(calendar.time)
            val dayLabel = when (i) {
                0 -> "Today"
                1 -> "Tomorrow"
                else -> "Day ${i + 1}"
            }
            result.add(Pair(dayLabel, dateText))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return result
    }
}
