package com.example.data.api

import com.example.model.MicroclimateWeatherData
import com.example.model.ThreatSeverity
import com.example.model.WeatherRiskThresholdAlert
import com.example.model.WeatherThresholdCategory
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

/**
 * Mock Weather Service providing localized agricultural microclimate weather data
 * and disease risk threshold evaluation algorithms.
 */
class MockWeatherService {

    private val sampleStations = listOf(
        MicroclimateWeatherData(
            stationId = "north_plot_a",
            stationName = "North Orchard & Vegetable Plot A",
            description = "High Humidity & Spore Inoculation Alert",
            temperatureC = 23.8f,
            relativeHumidityPercent = 89.5f,
            leafWetnessHours = 7.4f,
            rainfallMm = 18.2f,
            windSpeedKmh = 12.0f,
            dewPointC = 21.8f,
            uvIndex = 4.2f
        ),
        MicroclimateWeatherData(
            stationId = "south_polyhouse_b",
            stationName = "South Polyhouse & Arid Field B",
            description = "High Heatwave & Sucking Pest Proliferation",
            temperatureC = 35.6f,
            relativeHumidityPercent = 42.0f,
            leafWetnessHours = 0.5f,
            rainfallMm = 0.0f,
            windSpeedKmh = 18.5f,
            dewPointC = 19.2f,
            uvIndex = 9.0f
        ),
        MicroclimateWeatherData(
            stationId = "river_basin_c",
            stationName = "River Basin Lowland Plot C",
            description = "Saturated Soil & Dew Point Fungal Incubation",
            temperatureC = 18.5f,
            relativeHumidityPercent = 94.0f,
            leafWetnessHours = 9.8f,
            rainfallMm = 24.0f,
            windSpeedKmh = 6.5f,
            dewPointC = 17.5f,
            uvIndex = 3.0f
        ),
        MicroclimateWeatherData(
            stationId = "highland_terrace_d",
            stationName = "Highland Terrace Hillside D",
            description = "Moderate Breeze & Larval Emergence Window",
            temperatureC = 27.5f,
            relativeHumidityPercent = 72.0f,
            leafWetnessHours = 3.2f,
            rainfallMm = 4.0f,
            windSpeedKmh = 15.0f,
            dewPointC = 22.0f,
            uvIndex = 7.5f
        )
    )

    /**
     * Simulates network fetching of localized microclimate telemetry for a given plot station.
     */
    suspend fun fetchCurrentMicroclimate(stationId: String = "north_plot_a"): Result<MicroclimateWeatherData> {
        delay(400) // Simulate fast network round-trip
        val station = sampleStations.find { it.stationId == stationId } ?: sampleStations.first()
        return Result.success(station)
    }

    /**
     * Returns the list of predefined farm weather stations.
     */
    fun getAvailableStations(): List<MicroclimateWeatherData> {
        return sampleStations
    }

    /**
     * Evaluates agronomic disease and pest threshold rules against current weather parameters.
     */
    fun evaluateRiskThresholds(weather: MicroclimateWeatherData): List<WeatherRiskThresholdAlert> {
        val alerts = mutableListOf<WeatherRiskThresholdAlert>()

        // 1. High Humidity + Moderate Temp Threshold (>80% RH, 18-26°C): Late Blight & Downy Mildew
        if (weather.relativeHumidityPercent >= 80f && weather.temperatureC in 17f..27f) {
            alerts.add(
                WeatherRiskThresholdAlert(
                    alertId = "alert_late_blight_${weather.stationId}",
                    title = "Critical Late Blight & Downy Mildew Spore Alert",
                    targetDisease = "Late Blight (Phytophthora infestans) & Downy Mildew",
                    scientificName = "Phytophthora infestans / Pseudoperonospora cubensis",
                    affectedCrops = listOf("Tomato", "Potato", "Cucumber", "Grapes"),
                    severity = if (weather.relativeHumidityPercent >= 88f) ThreatSeverity.CRITICAL else ThreatSeverity.HIGH,
                    category = WeatherThresholdCategory.HUMIDITY_HIGH,
                    triggeredThresholdText = "Humidity ${weather.relativeHumidityPercent.toInt()}% (Threshold: >80%) • Temp ${weather.temperatureC}°C (18–26°C)",
                    riskMechanism = "Atmospheric relative humidity above 80% with canopy temperature between 18–26°C triggers exponential sporangia germination and leaf tissue penetration within 12–24 hours.",
                    ipmAdvisory = "Apply preventive bio-control (Trichoderma viride @ 5g/L) or protective Copper Oxychloride / Mancozeb. Avoid overhead sprinkler irrigation and ensure drainage furrow clearance.",
                    urgency = "Immediate Protective Action (Next 12 Hours)"
                )
            )
        }

        // 2. Prolonged Leaf Wetness Threshold (>6 hours): Bacterial Leaf Spot & Anthracnose
        if (weather.leafWetnessHours >= 6.0f) {
            alerts.add(
                WeatherRiskThresholdAlert(
                    alertId = "alert_leaf_wetness_${weather.stationId}",
                    title = "Prolonged Leaf Wetness - Bacterial Blight Alert",
                    targetDisease = "Bacterial Leaf Spot & Anthracnose",
                    scientificName = "Xanthomonas campestris / Colletotrichum gloeosporioides",
                    affectedCrops = listOf("Chilli", "Tomato", "Pomegranate", "Beans"),
                    severity = if (weather.leafWetnessHours >= 8f) ThreatSeverity.CRITICAL else ThreatSeverity.HIGH,
                    category = WeatherThresholdCategory.LEAF_WETNESS,
                    triggeredThresholdText = "Leaf Wetness ${weather.leafWetnessHours} hrs (Threshold: >6.0 hrs) • Rainfall ${weather.rainfallMm} mm",
                    riskMechanism = "Persistent liquid film on leaf surfaces for >6 hours allows bacterial flagellar motility into natural stomata and hydathodes, causing rapid water-soaked lesions.",
                    ipmAdvisory = "Prune lower senescent leaves for canopy aeration. Apply Streptocycline (0.5g/L) + Copper Hydroxide if bacterial lesions are spotted. Do not handle wet foliage to avoid spreading bacteria.",
                    urgency = "High Urgency (Next 24 Hours)"
                )
            )
        }

        // 3. High Temperature + Low-to-Moderate Humidity Threshold (>33°C): Whitefly, Thrip & Spider Mite Surge
        if (weather.temperatureC >= 33.0f) {
            alerts.add(
                WeatherRiskThresholdAlert(
                    alertId = "alert_heat_pest_${weather.stationId}",
                    title = "Heatwave Threshold - Sucking Pest Proliferation",
                    targetDisease = "Whitefly Vector & Chilli Thrips Multiplication",
                    scientificName = "Bemisia tabaci / Scirtothrips dorsalis",
                    affectedCrops = listOf("Cotton", "Chilli", "Tomato", "Okra"),
                    severity = if (weather.temperatureC >= 36f) ThreatSeverity.CRITICAL else ThreatSeverity.HIGH,
                    category = WeatherThresholdCategory.HEAT_DROUGHT_VECTOR,
                    triggeredThresholdText = "Temperature ${weather.temperatureC}°C (Threshold: >33°C) • Humidity ${weather.relativeHumidityPercent.toInt()}%",
                    riskMechanism = "High ambient heat drastically accelerates the insect lifecycle (egg-to-adult compressed from 21 days to 11 days), escalating viral transmission (Leaf Curl Virus).",
                    ipmAdvisory = "Install yellow sticky traps (15–20 per acre) and blue thrip cards. Spray cold-pressed Neem Oil 10,000 ppm (3 ml/L) or Beauveria bassiana bio-insecticide during early morning hours.",
                    urgency = "Proactive Vector Control (Next 24–48 Hours)"
                )
            )
        }

        // 4. Dew Point & High Night Moisture (Dew Point near Temp, RH > 90%): Powdery Mildew & Early Blight
        if (weather.relativeHumidityPercent >= 90f && weather.leafWetnessHours in 3f..8f && weather.temperatureC in 15f..25f) {
            alerts.add(
                WeatherRiskThresholdAlert(
                    alertId = "alert_dew_mildew_${weather.stationId}",
                    title = "Dew Point Condensation - Early Blight & Mildew",
                    targetDisease = "Early Blight & Powdery Mildew",
                    scientificName = "Alternaria solani / Erysiphe cichoracearum",
                    affectedCrops = listOf("Tomato", "Potato", "Eggplant", "Melons"),
                    severity = ThreatSeverity.HIGH,
                    category = WeatherThresholdCategory.DEW_POINT_INCUBATION,
                    triggeredThresholdText = "Dew Point ${weather.dewPointC}°C ≈ Temp ${weather.temperatureC}°C • Humidity ${weather.relativeHumidityPercent.toInt()}%",
                    riskMechanism = "Heavy diurnal dew condensation combined with morning warming creates cyclic drying-wetting stress, encouraging concentric target-spot conidia sporulation.",
                    ipmAdvisory = "Apply prophylactic bio-fungicide (Bacillus subtilis @ 3g/L) or systemic Azoxystrobin 23% SC (1 ml/L). Ensure wide ridge planting for morning sunlight penetration.",
                    urgency = "Recommended Precaution"
                )
            )
        }

        // 5. High Rainfall & Waterlogging Threshold (>15 mm rain or RH > 92%): Root Rot & Damping-off
        if (weather.rainfallMm >= 15.0f || (weather.relativeHumidityPercent >= 92f && weather.leafWetnessHours >= 8f)) {
            alerts.add(
                WeatherRiskThresholdAlert(
                    alertId = "alert_root_rot_${weather.stationId}",
                    title = "Rainfall & Soil Saturation - Root Rot / Damping-Off",
                    targetDisease = "Pythium Damping-off & Sclerotinia Stem Rot",
                    scientificName = "Pythium debaryanum / Sclerotinia sclerotiorum",
                    affectedCrops = listOf("Seedlings", "Cabbage", "Chilli", "Tomato"),
                    severity = ThreatSeverity.HIGH,
                    category = WeatherThresholdCategory.PRECIPITATION_WASH,
                    triggeredThresholdText = "Precipitation ${weather.rainfallMm} mm (Threshold: >15mm) • RH ${weather.relativeHumidityPercent.toInt()}%",
                    riskMechanism = "Soil waterlogging displaces rhizosphere oxygen, causing anaerobic conditions that enable motile Pythium zoospores to infect root collars and stem bases.",
                    ipmAdvisory = "Drench nursery beds and root zones with Trichoderma harzianum or Copper Oxychloride 50% WP @ 3g/L. Ensure furrow runoff channels are clear of silt blockage.",
                    urgency = "Drainage & Drenching Required"
                )
            )
        }

        // If no high thresholds are met, return a baseline low-risk monitoring alert
        if (alerts.isEmpty()) {
            alerts.add(
                WeatherRiskThresholdAlert(
                    alertId = "alert_normal_safe_${weather.stationId}",
                    title = "Favorable Microclimate - Standard Field Monitoring",
                    targetDisease = "General Pathogen Monitoring",
                    scientificName = "Standard Agronomic Baseline",
                    affectedCrops = listOf("All Crop Fields"),
                    severity = ThreatSeverity.LOW,
                    category = WeatherThresholdCategory.HUMIDITY_HIGH,
                    triggeredThresholdText = "All weather metrics within standard agronomic safety thresholds",
                    riskMechanism = "Current temperature (${weather.temperatureC}°C) and relative humidity (${weather.relativeHumidityPercent.toInt()}%) do not present immediate epidemic pathogen pressure.",
                    ipmAdvisory = "Maintain routine weekly scouting, pheromone trap counts, and clean agronomic field hygiene.",
                    urgency = "Routine Observation"
                )
            )
        }

        return alerts
    }

    /**
     * Generates a simulated microclimate condition to test threshold responses dynamically.
     */
    fun createSimulatedReading(
        temp: Float,
        humidity: Float,
        leafWetness: Float,
        rain: Float
    ): MicroclimateWeatherData {
        val clampedTemp = min(max(temp, 10f), 50f)
        val clampedHumidity = min(max(humidity, 10f), 100f)
        val clampedLeafWetness = min(max(leafWetness, 0f), 24f)
        val clampedRain = min(max(rain, 0f), 100f)
        
        // Approximate dew point
        val dewPoint = clampedTemp - ((100f - clampedHumidity) / 5f)

        return MicroclimateWeatherData(
            stationId = "custom_simulation",
            stationName = "Custom Weather Threshold Simulator",
            description = "Simulated field microclimate parameters",
            temperatureC = clampedTemp,
            relativeHumidityPercent = clampedHumidity,
            leafWetnessHours = clampedLeafWetness,
            rainfallMm = clampedRain,
            windSpeedKmh = 10.0f,
            dewPointC = dewPoint,
            uvIndex = 6.0f
        )
    }
}
