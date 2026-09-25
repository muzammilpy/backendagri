package com.example.model

import androidx.compose.ui.graphics.Color

enum class WeatherThresholdCategory(
    val label: String,
    val iconName: String
) {
    HUMIDITY_HIGH("High Humidity & Spore Surge", "WaterDrop"),
    LEAF_WETNESS("Prolonged Leaf Wetness", "Opacity"),
    HEAT_DROUGHT_VECTOR("High Temp Pest Proliferation", "WbSunny"),
    DEW_POINT_INCUBATION("Dew Point & Cool Moisture", "SevereCold"),
    PRECIPITATION_WASH("Rainfall Splash & Root Saturation", "Thunderstorm")
}

data class MicroclimateWeatherData(
    val stationId: String,
    val stationName: String,
    val description: String,
    val temperatureC: Float,
    val relativeHumidityPercent: Float,
    val leafWetnessHours: Float,
    val rainfallMm: Float,
    val windSpeedKmh: Float,
    val dewPointC: Float,
    val uvIndex: Float = 6.0f,
    val timestamp: Long = System.currentTimeMillis()
)

data class WeatherRiskThresholdAlert(
    val alertId: String,
    val title: String,
    val targetDisease: String,
    val scientificName: String,
    val affectedCrops: List<String>,
    val severity: ThreatSeverity,
    val category: WeatherThresholdCategory,
    val triggeredThresholdText: String,
    val riskMechanism: String,
    val ipmAdvisory: String,
    val urgency: String = "Immediate Action Recommended (Next 12–24h)"
)
