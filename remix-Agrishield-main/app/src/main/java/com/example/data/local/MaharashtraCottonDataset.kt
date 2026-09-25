package com.example.data.local

import com.example.model.ThreatSeverity

data class CottonDistrictRecord(
    val districtName: String,
    val belt: String,
    val latitude: Double,
    val longitude: Double,
    val soilClassification: String,
    val dominantGrowthStage: String,
    val averageNdvi: Float,
    val isStressed: Boolean, // NDVI < 0.50
    val primaryPestThreat: String,
    val outbreakRisk: ThreatSeverity,
    val samplePestCount: Int,
    val activeAdvisory: String
)

object MaharashtraCottonDataset {

    val coordinateBounds = mapOf(
        "latMin" to 18.9,
        "latMax" to 21.3,
        "lonMin" to 75.2,
        "lonMax" to 78.8
    )

    val districts: List<CottonDistrictRecord> = listOf(
        CottonDistrictRecord(
            districtName = "Yavatmal",
            belt = "Vidarbha",
            latitude = 20.3888,
            longitude = 78.1204,
            soilClassification = "Deep Vertisols (Black Cotton Soil)",
            dominantGrowthStage = "Boll Development & Maturation",
            averageNdvi = 0.44f,
            isStressed = true,
            primaryPestThreat = "Pink Bollworm (Pectinophora gossypiella)",
            outbreakRisk = ThreatSeverity.CRITICAL,
            samplePestCount = 22,
            activeAdvisory = "NDVI < 0.50 detected across 42% canopy. Rosetted flowers and boll penetration noted. Immediate mating disruption & Trichogramma release required."
        ),
        CottonDistrictRecord(
            districtName = "Akola",
            belt = "Vidarbha",
            latitude = 20.7059,
            longitude = 77.0219,
            soilClassification = "Medium Black Soil",
            dominantGrowthStage = "Flowering & Square Formation",
            averageNdvi = 0.58f,
            isStressed = false,
            primaryPestThreat = "Cotton Aphids (Aphis gossypii)",
            outbreakRisk = ThreatSeverity.MODERATE,
            samplePestCount = 9,
            activeAdvisory = "Canopy vigor stable. Early leaf curling on lower canopy due to aphid honeydew. Apply Neem oil 10,000 ppm if sticky deposit spreads."
        ),
        CottonDistrictRecord(
            districtName = "Wardha",
            belt = "Vidarbha",
            latitude = 20.7453,
            longitude = 78.6022,
            soilClassification = "Deep Vertisols",
            dominantGrowthStage = "Boll Development",
            averageNdvi = 0.48f,
            isStressed = true,
            primaryPestThreat = "American Bollworm (Helicoverpa armigera)",
            outbreakRisk = ThreatSeverity.HIGH,
            samplePestCount = 16,
            activeAdvisory = "Bored holes in bolls with frass observed. NDVI 0.48 indicates localized water & pest stress. Deploy pheromone traps @ 5/acre."
        ),
        CottonDistrictRecord(
            districtName = "Amravati",
            belt = "Vidarbha",
            latitude = 20.9374,
            longitude = 77.7796,
            soilClassification = "Deep Black Cotton Soil",
            dominantGrowthStage = "Flowering Stage",
            averageNdvi = 0.52f,
            isStressed = false,
            primaryPestThreat = "Whitefly (Bemisia tabaci)",
            outbreakRisk = ThreatSeverity.MODERATE,
            samplePestCount = 11,
            activeAdvisory = "Whitefly adults active on leaf undersides. Install yellow sticky traps (15-20/acre) to prevent CLCuV transmission."
        ),
        CottonDistrictRecord(
            districtName = "Jalgaon",
            belt = "Khandesh",
            latitude = 21.0077,
            longitude = 75.5626,
            soilClassification = "Deep Alluvial & Medium Black",
            dominantGrowthStage = "Boll Development",
            averageNdvi = 0.42f,
            isStressed = true,
            primaryPestThreat = "Pink Bollworm (Pectinophora gossypiella)",
            outbreakRisk = ThreatSeverity.CRITICAL,
            samplePestCount = 26,
            activeAdvisory = "Critical outbreak hotspot in Khandesh belt. Severe lint staining and rosette blooms. Mandate Quinalphos/Emamectin targeted spray."
        ),
        CottonDistrictRecord(
            districtName = "Nanded",
            belt = "Marathwada",
            latitude = 19.1383,
            longitude = 77.3210,
            soilClassification = "Medium Black Soil",
            dominantGrowthStage = "Flowering & Boll Initiation",
            averageNdvi = 0.55f,
            isStressed = false,
            primaryPestThreat = "Thrips (Thrips tabaci)",
            outbreakRisk = ThreatSeverity.MODERATE,
            samplePestCount = 8,
            activeAdvisory = "Silvery spots on leaf undersides and crinkled leaves. Spray Spinosad 45% SC or Fipronil if thrips exceed 10/leaf threshold."
        ),
        CottonDistrictRecord(
            districtName = "Jalna",
            belt = "Marathwada",
            latitude = 19.8410,
            longitude = 75.8863,
            soilClassification = "Medium to Shallow Black Soil",
            dominantGrowthStage = "Square Formation",
            averageNdvi = 0.47f,
            isStressed = true,
            primaryPestThreat = "American Bollworm (Helicoverpa armigera)",
            outbreakRisk = ThreatSeverity.HIGH,
            samplePestCount = 14,
            activeAdvisory = "NDVI 0.47 flags vegetative stress. Larval feeding on flower buds (squares) detected. Release Trichogramma egg parasitoids."
        ),
        CottonDistrictRecord(
            districtName = "Parbhani",
            belt = "Marathwada",
            latitude = 19.2611,
            longitude = 76.7767,
            soilClassification = "Deep Black Vertisols",
            dominantGrowthStage = "Boll Development",
            averageNdvi = 0.49f,
            isStressed = true,
            primaryPestThreat = "Whitefly & Cotton Aphids",
            outbreakRisk = ThreatSeverity.HIGH,
            samplePestCount = 17,
            activeAdvisory = "Honeydew accumulation causing sooty mold growth. Combined whitefly and aphid pressure under warm humid conditions."
        )
    )

    fun generateSyntheticSimulation(): List<CottonDistrictRecord> {
        return districts.map { record ->
            val randomVariation = (Math.random() * 0.1 - 0.05).toFloat()
            val newNdvi = (record.averageNdvi + randomVariation).coerceIn(0.30f, 0.75f)
            val isStressed = newNdvi < 0.50f
            val newPestCount = (record.samplePestCount + (Math.random() * 6 - 3).toInt()).coerceAtLeast(2)
            val newRisk = when {
                newNdvi < 0.45f || newPestCount >= 20 -> ThreatSeverity.CRITICAL
                newNdvi < 0.52f || newPestCount >= 12 -> ThreatSeverity.HIGH
                newPestCount >= 6 -> ThreatSeverity.MODERATE
                else -> ThreatSeverity.LOW
            }
            record.copy(
                averageNdvi = String.format(java.util.Locale.US, "%.2f", newNdvi).toFloat(),
                isStressed = isStressed,
                samplePestCount = newPestCount,
                outbreakRisk = newRisk
            )
        }
    }
}
