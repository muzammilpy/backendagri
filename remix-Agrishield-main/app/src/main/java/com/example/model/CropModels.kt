package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

enum class ReportStatus(val label: String) {
    AI_DIAGNOSED("AI Diagnosed"),
    SUBMITTED_TO_EXPERT("Submitted to Extension"),
    IN_LAB_REVIEW("In Laboratory Triage"),
    EXPERT_VALIDATED("Expert Confirmed"),
    RESOLVED("Treatment Completed")
}

enum class ThreatSeverity(val label: String) {
    LOW("Low Risk"),
    MODERATE("Moderate Risk"),
    HIGH("High Outbreak"),
    CRITICAL("Severe Alert")
}

enum class TrapType(val label: String) {
    PHEROMONE("Pheromone Trap"),
    YELLOW_STICKY("Yellow Sticky Card"),
    LIGHT_TRAP("Light Trap"),
    PITFALL("Pitfall Trap")
}

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    MARATHI("mr", "Marathi", "मराठी"),
    SPANISH("es", "Spanish", "Español"),
    SWAHILI("sw", "Swahili", "Kiswahili"),
    BENGALI("bn", "Bengali", "বাংলা"),
    FRENCH("fr", "French", "Français");

    companion object {
        val farmerFacing: List<AppLanguage> = listOf(ENGLISH, HINDI, MARATHI)

        fun fromCode(code: String?): AppLanguage {
            if (code == null) return ENGLISH
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}

enum class FieldOutcome(val label: String) {
    NONE("Not confirmed"),
    SUCCESS("Treatment helped"),
    PARTIAL("Partial control"),
    FAILED("Did not work")
}

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Long = 1L,
    val name: String = "Farmer",
    val role: String = "Farmer / Farm Owner",
    val location: String = "Yavatmal, Vidarbha, Maharashtra",
    val phone: String = "",
    val selectedLanguageCode: String = "en",
    val staffUnlocked: Boolean = false
)

fun computeInitials(name: String): String {
    val trimmed = name.trim()
    if (trimmed.isEmpty()) return "F"
    val parts = trimmed.split("\\s+".toRegex()).filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
        parts.size == 1 && parts[0].equals("Farmer", ignoreCase = true) -> "F"
        parts.size == 1 && parts[0].length >= 2 -> "${parts[0][0].uppercaseChar()}${parts[0][1].uppercaseChar()}"
        parts.size == 1 -> parts[0].first().uppercaseChar().toString()
        else -> "F"
    }
}

@Entity(tableName = "crop_reports")
data class CropReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val farmerName: String = "Farm Unit #1",
    val villageRegion: String = "Green Valley, Ward 4",
    val cropName: String,
    val variety: String,
    val cropStage: String,
    val soilCondition: String,
    val weatherFactors: String,
    val trapCount: Int = 0,
    val sensorMoisture: Float = 45f,
    val sensorTemp: Float = 28f,
    val symptomDescription: String,
    val sampleImageKey: String = "sample_leaf",
    val customImageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    
    // Diagnosis Data
    val diseaseName: String,
    val scientificName: String,
    val confidence: Int, // e.g. 94%
    val severity: ThreatSeverity,
    val causeSummary: String,
    val weatherRiskCorrelation: String,
    
    // Integrated Pest Management (IPM)
    val ipmBiological: String,
    val ipmCultural: String,
    val ipmChemical: String,
    val safeDosage: String,
    val preHarvestIntervalDays: Int, // PHI in days
    val safetyPpeInstructions: String,
    val labReferralRecommended: Boolean,
    
    // Status and Expert Triage
    val status: ReportStatus = ReportStatus.AI_DIAGNOSED,
    val expertNotes: String = "",
    val expertName: String = "",
    val labDiagnosisReport: String = "",
    val validationTimestamp: Long = 0L,
    
    // Follow-up Monitoring Checkpoints
    val followUpDay3Checked: Boolean = false,
    val followUpDay7Checked: Boolean = false,
    val followUpDay14Checked: Boolean = false,
    val followUpNotes: String = "",
    
    // Geospatial Coordinates
    val latitude: Double = 20.3888,
    val longitude: Double = 78.1204,

    val diagnosisSource: String = "Not specified",
    val imageWasAnalyzed: Boolean = false,
    val fieldOutcome: String = "NONE",
    val isReferenceCase: Boolean = false
)

@Entity(tableName = "pest_traps")
data class PestTrapLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trapType: TrapType,
    val targetPest: String,
    val cropField: String,
    val count: Int,
    val economicThresholdLimit: Int,
    val thresholdExceeded: Boolean,
    val notes: String,
    val timestamp: Long = System.currentTimeMillis(),
    val locationName: String = "North Plot A"
)

@Entity(tableName = "sensor_readings")
data class SensorReading(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fieldName: String,
    val soilMoisturePercent: Float,
    val canopyTempC: Float,
    val relativeHumidityPercent: Float,
    val leafWetnessHours: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val isAnomaly: Boolean = false
)

@Entity(tableName = "hotspot_zones")
data class HotspotZone(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val zoneName: String,
    val crop: String,
    val threatName: String,
    val severity: ThreatSeverity,
    val confirmedReportsCount: Int,
    val latitude: Double,
    val longitude: Double,
    val radiusKm: Float,
    val advisoryNotice: String,
    val lastUpdated: Long = System.currentTimeMillis(),
    val dataProvenance: String = "Device reports and trap logs"
)

@Entity(tableName = "official_alerts")
data class OfficialAlert(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val broadcastZone: String,
    val targetCrops: String,
    val severity: ThreatSeverity,
    val message: String,
    val issuedBy: String,
    val date: String,
    val actionRequired: String,
    val isRead: Boolean = false
)

data class WeatherRiskForecast(
    val dayLabel: String,
    val dateText: String,
    val tempMax: Int,
    val tempMin: Int,
    val humidityPercent: Int,
    val rainProbabilityPercent: Int,
    val conditionDescription: String,
    val fungalSporeRisk: ThreatSeverity,
    val pestVectorRisk: ThreatSeverity,
    val actionableAdvisory: String
)

data class AIDiagnosisResponse(
    val isValidPlant: Boolean = true,
    val rejectionReason: String = "",
    val cropName: String = "Tomato",
    val variety: String = "Abhinav F1",
    val growthStage: String = "Fruiting Stage",
    val symptomsSummary: String = "Dark brown water-soaked blotches on leaves with faint white downy fuzz on underside.",
    val diseaseName: String = "Late Blight (Phytophthora infestans)",
    val scientificName: String = "Phytophthora infestans",
    val confidence: Int = 92,
    val severity: String = "HIGH",
    val causeSummary: String = "Fungal-like oomycete pathogen accelerated by relative humidity > 85% and cool canopy moisture.",
    val weatherRiskCorrelation: String = "Recent rainfall and 88% humidity create high microclimate spore germination within 24-48 hours.",
    val ipmBiological: String = "Apply Trichoderma viride or Bacillus subtilis bio-fungicide slurry at base of stems.",
    val ipmCultural: String = "Increase plant spacing for aeration, prune lower infected foliage, avoid overhead sprinkler irrigation.",
    val ipmChemical: String = "Targeted curative: Metalaxyl 8% + Mancozeb 64% WP at 2.5 g/L water or Cymoxanil.",
    val safeDosage: String = "2.5 g per Litre (500 g per Acre in 200L water)",
    val preHarvestIntervalDays: Int = 7,
    val safetyPpeInstructions: String = "Wear nitrile gloves, eye goggles, and N95 face mask. Wash spray equipment away from water bodies.",
    val labReferralRecommended: Boolean = true,
    val followUpAdvice: String = "Re-inspect undersides of leaves in 3 days. If white sporulation persists, apply second systemic rotation.",
    val diagnosisSource: String = com.example.util.AgriShieldConfig.RULE_ENGINE_SOURCE,
    val imageWasAnalyzed: Boolean = false
)
