package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CropReport::class,
        PestTrapLog::class,
        SensorReading::class,
        HotspotZone::class,
        OfficialAlert::class,
        UserProfile::class
    ],
    version = 3,
    exportSchema = false
)
abstract class CropDatabase : RoomDatabase() {
    abstract fun cropReportDao(): CropReportDao
    abstract fun pestTrapDao(): PestTrapDao
    abstract fun sensorDao(): SensorDao
    abstract fun hotspotDao(): HotspotDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: CropDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CropDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CropDatabase::class.java,
                    "crop_health_db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(CropDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class CropDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        suspend fun populateInitialData(db: CropDatabase) {
            val now = System.currentTimeMillis()

            // Seed Initial Crop Reports
            val sampleReports = listOf(
                CropReport(
                    farmerName = "Rajesh Patel (Plot 3B)",
                    villageRegion = "Nashik vegetable belt (reference case)",
                    cropName = "Tomato",
                    variety = "Abhinav F1",
                    cropStage = "Fruiting Stage",
                    soilCondition = "Loamy, High Moisture (82%)",
                    weatherFactors = "Intermittent heavy rains, 90% humidity",
                    trapCount = 4,
                    sensorMoisture = 78f,
                    sensorTemp = 24.5f,
                    symptomDescription = "Dark water-soaked brown lesions on leaves and fruit with white fungal downy growth on undersides.",
                    sampleImageKey = "tomato_late_blight",
                    timestamp = now - 3600000 * 5,
                    diseaseName = "Late Blight (Phytophthora infestans)",
                    scientificName = "Phytophthora infestans",
                    confidence = 94,
                    severity = ThreatSeverity.CRITICAL,
                    causeSummary = "Oomycete pathogen stimulated by saturated canopy moisture and persistent overcast humidity.",
                    weatherRiskCorrelation = "Current 90% humidity and rain window triggers rapid secondary sporangia dispersal.",
                    ipmBiological = "Trichoderma harzianum soil drench (5g/L) + Pseudomonas fluorescens foliar spray.",
                    ipmCultural = "Stake vines to improve air circulation, rogue and destroy heavily blighted plants, avoid sprinkler overhead watering.",
                    ipmChemical = "Targeted curative: Cymoxanil 8% + Mancozeb 64% WP at 2.0-2.5 g/L or Dimethomorph 50% WP.",
                    safeDosage = "2.5 g / Litre (500g in 200L water per acre)",
                    preHarvestIntervalDays = 7,
                    safetyPpeInstructions = "Full PPE mandatory: Respirator mask, rubber gloves, protective goggles. Spray downwind only.",
                    labReferralRecommended = true,
                    status = ReportStatus.EXPERT_VALIDATED,
                    expertName = "Dr. Anita Rao (Senior Extension Officer)",
                    expertNotes = "Confirmed Phytophthora sporangia under microscopy. Advised immediate localized systemic spray before tomorrow's predicted rain.",
                    validationTimestamp = now - 3600000 * 2,
                    followUpDay3Checked = true,
                    followUpDay7Checked = false,
                    followUpNotes = "Day 3 check: lesion spread halted on upper canopy. Fruit rot contained.",
                    latitude = 19.9975,
                    longitude = 73.7898,
                    diagnosisSource = "Reference case for SIH demo (not a live field upload)",
                    isReferenceCase = true,
                    fieldOutcome = "PARTIAL"
                ),
                CropReport(
                    farmerName = "Sanjay Jadhav (Field 1)",
                    villageRegion = "Ahmednagar maize block (reference case)",
                    cropName = "Maize (Corn)",
                    variety = "DKC 9108",
                    cropStage = "Whorl / Vegetative",
                    soilCondition = "Well-drained Sandy Loam",
                    weatherFactors = "Warm & Dry, 31°C",
                    trapCount = 18,
                    sensorMoisture = 42f,
                    sensorTemp = 31.0f,
                    symptomDescription = "Extensive window-pane feeding on whorl leaves with moist sawdust-like frass inside the leaf funnel.",
                    sampleImageKey = "maize_fall_armyworm",
                    timestamp = now - 3600000 * 24,
                    diseaseName = "Fall Armyworm (Spodoptera frugiperda)",
                    scientificName = "Spodoptera frugiperda",
                    confidence = 91,
                    severity = ThreatSeverity.HIGH,
                    causeSummary = "Nocturnal lepidopteran pest larvae feeding inside central whorls.",
                    weatherRiskCorrelation = "Warm night temperatures accelerating larval instars and moth oviposition.",
                    ipmBiological = "Release Trichogramma pretiosum parasitoid wasps; spray Bacillus thuringiensis (Bt) aizawai @ 2g/L.",
                    ipmCultural = "Erect 5-8 pheromone traps/acre; intercrop with Desmodium (push-pull strategy); hand-pick egg masses.",
                    ipmChemical = "Chlorantraniliprole 18.5% SC @ 0.4 ml/L or Emamectin benzoate 5% SG @ 0.4 g/L applied directly into whorl.",
                    safeDosage = "0.4 ml / Litre directly aimed into leaf whorls",
                    preHarvestIntervalDays = 14,
                    safetyPpeInstructions = "Wear protective chemical suit, boots and gloves. Do not apply during honeybee foraging hours.",
                    labReferralRecommended = false,
                    status = ReportStatus.SUBMITTED_TO_EXPERT,
                    expertName = "",
                    expertNotes = "",
                    validationTimestamp = 0L,
                    latitude = 19.0952,
                    longitude = 74.7496,
                    diagnosisSource = "Reference case for SIH demo (not a live field upload)",
                    isReferenceCase = true
                ),
                CropReport(
                    farmerName = "Suresh Kumar",
                    villageRegion = "Kolhapur paddy tract (reference case)",
                    cropName = "Rice (Paddy)",
                    variety = "Swarna Sub-1",
                    cropStage = "Tillering to Panicle Initiation",
                    soilCondition = "Submerged Clay Puddle",
                    weatherFactors = "High humidity 86%, overcast skies",
                    trapCount = 2,
                    sensorMoisture = 95f,
                    sensorTemp = 27.2f,
                    symptomDescription = "Spindle-shaped diamond eye lesions with grey centers and brown borders on leaf blades.",
                    sampleImageKey = "rice_blast",
                    timestamp = now - 3600000 * 48,
                    diseaseName = "Rice Blast (Magnaporthe oryzae)",
                    scientificName = "Magnaporthe oryzae",
                    confidence = 96,
                    severity = ThreatSeverity.HIGH,
                    causeSummary = "Airborne fungal pathogen taking advantage of high canopy dew duration and excessive nitrogen fertilizer.",
                    weatherRiskCorrelation = "Dew period > 10 hours and cloud cover creates ideal conditions for conidial attachment.",
                    ipmBiological = "Foliar application of Pseudomonas fluorescens (10g/L) or neem seed kernel extract (5%).",
                    ipmCultural = "Split nitrogen top-dressing; avoid excess urea; maintain optimum water depth without stagnant stress.",
                    ipmChemical = "Tricyclazole 75% WP @ 0.6 g/L or Azoxystrobin 18.2% + Difenoconazole 11.4% SC @ 1.0 ml/L.",
                    safeDosage = "0.6 g / Litre (120 g in 200 L water per acre)",
                    preHarvestIntervalDays = 21,
                    safetyPpeInstructions = "Wash exposed skin immediately after application. Do not contaminate irrigation canals.",
                    labReferralRecommended = true,
                    status = ReportStatus.EXPERT_VALIDATED,
                    expertName = "Dr. Anita Rao",
                    expertNotes = "Leaf blast confirmed. Advised immediate potassium supplementation alongside targeted Tricyclazole.",
                    validationTimestamp = now - 3600000 * 36,
                    followUpDay3Checked = true,
                    followUpDay7Checked = true,
                    followUpDay14Checked = false,
                    followUpNotes = "Lesions dried out into necrotic scars. Panicles emerging healthy.",
                    latitude = 16.7050,
                    longitude = 74.2433,
                    diagnosisSource = "Reference case for SIH demo (not a live field upload)",
                    isReferenceCase = true,
                    fieldOutcome = "SUCCESS"
                )
            )

            sampleReports.forEach { db.cropReportDao().insertReport(it) }

            // Seed Pest Trap Logs
            val trapLogs = listOf(
                PestTrapLog(
                    trapType = TrapType.PHEROMONE,
                    targetPest = "Fall Armyworm (Spodoptera)",
                    cropField = "Maize Block East",
                    count = 22,
                    economicThresholdLimit = 10,
                    thresholdExceeded = true,
                    notes = "Exceeded Economic Threshold Level (ETL). Immediate biological/chemical whorl application required.",
                    timestamp = now - 3600000 * 6,
                    locationName = "East Field 4"
                ),
                PestTrapLog(
                    trapType = TrapType.YELLOW_STICKY,
                    targetPest = "Whitefly & Thrips",
                    cropField = "Chili & Tomato Nursery",
                    count = 45,
                    economicThresholdLimit = 30,
                    thresholdExceeded = true,
                    notes = "High whitefly vector density. Risk of Tomato Leaf Curl Virus transmission elevated.",
                    timestamp = now - 3600000 * 18,
                    locationName = "Nursery Zone A"
                ),
                PestTrapLog(
                    trapType = TrapType.PHEROMONE,
                    targetPest = "Pink Bollworm (Pectinophora)",
                    cropField = "Cotton Field South",
                    count = 6,
                    economicThresholdLimit = 8,
                    thresholdExceeded = false,
                    notes = "Below ETL threshold. Continue weekly monitoring and replace pheromone lure in 10 days.",
                    timestamp = now - 3600000 * 30,
                    locationName = "South Plot 2"
                ),
                PestTrapLog(
                    trapType = TrapType.LIGHT_TRAP,
                    targetPest = "Brown Planthopper (BPH)",
                    cropField = "Paddy Lowland",
                    count = 120,
                    economicThresholdLimit = 100,
                    thresholdExceeded = true,
                    notes = "Hopper burn risk in dense tiller canopy. Advised intermittent drying of paddy field.",
                    timestamp = now - 3600000 * 12,
                    locationName = "Delta Sector B"
                )
            )

            trapLogs.forEach { db.pestTrapDao().insertTrapLog(it) }

            // Seed Hotspot Zones
            val hotspots = listOf(
                HotspotZone(
                    zoneName = "Zone Alpha - Khed Ridge",
                    crop = "Tomato & Potato",
                    threatName = "Late Blight Epidemic Cluster",
                    severity = ThreatSeverity.CRITICAL,
                    confirmedReportsCount = 24,
                    latitude = 18.5250,
                    longitude = 73.8500,
                    radiusKm = 3.5f,
                    advisoryNotice = "High spore risk. All solanaceous growers within 4km must apply preventative systemic spray before rain.",
                    lastUpdated = now,
                    dataProvenance = "Reference cluster for demo — not a live outbreak feed"
                ),
                HotspotZone(
                    zoneName = "Zone Beta - East Valley",
                    crop = "Maize / Corn",
                    threatName = "Fall Armyworm Outbreak",
                    severity = ThreatSeverity.HIGH,
                    confirmedReportsCount = 17,
                    latitude = 18.5380,
                    longitude = 73.8640,
                    radiusKm = 5.0f,
                    advisoryNotice = "Pheromone traps exceed 15 moths/night. Whorl inspection and Bt bio-spraying recommended immediately.",
                    lastUpdated = now
                ),
                HotspotZone(
                    zoneName = "Zone Gamma - Delta Lowlands",
                    crop = "Rice (Paddy)",
                    threatName = "Bacterial Leaf Streak & Blast",
                    severity = ThreatSeverity.MODERATE,
                    confirmedReportsCount = 9,
                    latitude = 18.5120,
                    longitude = 73.8420,
                    radiusKm = 2.8f,
                    advisoryNotice = "Localized leaf blast detected. Avoid excess nitrogen top-dressing and spray Tricyclazole.",
                    lastUpdated = now
                ),
                HotspotZone(
                    zoneName = "Zone Delta - Southern Uplands",
                    crop = "Cotton",
                    threatName = "Pink Bollworm Surveillance",
                    severity = ThreatSeverity.LOW,
                    confirmedReportsCount = 3,
                    latitude = 18.5020,
                    longitude = 73.8750,
                    radiusKm = 2.0f,
                    advisoryNotice = "Routine trap counts below threshold. Routine biological monitoring active.",
                    lastUpdated = now
                )
            )

            db.hotspotDao().insertAll(hotspots)

            // Seed Maharashtra Cotton GIS & Pest Infestation Dataset Integration (Vidarbha, Khandesh, Marathwada)
            val cottonDistricts = MaharashtraCottonDataset.districts
            val cottonHotspots = cottonDistricts.map { record ->
                HotspotZone(
                    zoneName = "${record.districtName} Belt (${record.belt})",
                    crop = "Cotton (Hybrid Bt)",
                    threatName = record.primaryPestThreat,
                    severity = record.outbreakRisk,
                    confirmedReportsCount = record.samplePestCount,
                    latitude = record.latitude,
                    longitude = record.longitude,
                    radiusKm = 4.0f,
                    advisoryNotice = record.activeAdvisory,
                    lastUpdated = now,
                    dataProvenance = "District reference snapshot for SIH demo — not live satellite NDVI"
                )
            }
            db.hotspotDao().insertAll(cottonHotspots)

            val cottonReports = cottonDistricts.mapIndexed { index, record ->
                CropReport(
                    farmerName = "Farmer Unit - ${record.districtName}",
                    villageRegion = "${record.districtName} District (${record.belt} Belt)",
                    cropName = "Cotton",
                    variety = "Bt Cotton (MECH-1 / Bunny)",
                    cropStage = record.dominantGrowthStage,
                    soilCondition = "${record.soilClassification} | canopy index (static demo): ${record.averageNdvi}",
                    weatherFactors = "Temperature: 31°C, Humidity: 68%, Wind: 14 km/h",
                    trapCount = record.samplePestCount,
                    sensorMoisture = if (record.isStressed) 38f else 55f,
                    sensorTemp = 32.5f,
                    symptomDescription = "Reference scouting note for ${record.districtName}: ${record.primaryPestThreat}. Trap count sample ${record.samplePestCount}.",
                    sampleImageKey = "cotton_pest_sample",
                    timestamp = now - 3600000L * (index + 1),
                    diseaseName = record.primaryPestThreat,
                    scientificName = when {
                        record.primaryPestThreat.contains("Pink") -> "Pectinophora gossypiella"
                        record.primaryPestThreat.contains("American") -> "Helicoverpa armigera"
                        record.primaryPestThreat.contains("Whitefly") -> "Bemisia tabaci"
                        record.primaryPestThreat.contains("Aphids") -> "Aphis gossypii"
                        else -> "Thrips tabaci"
                    },
                    confidence = 94,
                    severity = record.outbreakRisk,
                    causeSummary = "Reference record for Maharashtra cotton belt (${record.belt}). Not a live satellite product.",
                    weatherRiskCorrelation = "Monsoon dry spells followed by high temperature humidity spikes favoring pest oviposition.",
                    ipmBiological = "Release Trichogramma egg parasitoids @ 50,000/acre and spray Beauveria bassiana.",
                    ipmCultural = "Deep summer ploughing, removal of crop debris, and adoption of resistant Bt hybrids.",
                    ipmChemical = "Targeted bio-rational or selective insecticide upon exceeding Economic Threshold Level (ETL).",
                    safeDosage = "2.0 ml / Litre water (500L per hectare)",
                    preHarvestIntervalDays = 14,
                    safetyPpeInstructions = "Wear protective face mask, rubber gloves and apron during spray operations.",
                    labReferralRecommended = record.outbreakRisk == ThreatSeverity.CRITICAL,
                    status = ReportStatus.EXPERT_VALIDATED,
                    expertName = "Dr. V. K. Deshmukh (Maharashtra Cotton Agronomy Specialist)",
                    expertNotes = "Reference specialist note for demo. Coordinates: ${record.latitude}°N, ${record.longitude}°E.",
                    validationTimestamp = now - 3600000L,
                    latitude = record.latitude,
                    longitude = record.longitude,
                    diagnosisSource = "District reference snapshot (demo)",
                    isReferenceCase = true
                )
            }
            cottonReports.forEach { db.cropReportDao().insertReport(it) }

            // Seed Sensor Readings
            val sensorLogs = listOf(
                SensorReading(
                    fieldName = "Plot 1 - Tomato Canopy",
                    soilMoisturePercent = 84f,
                    canopyTempC = 23.8f,
                    relativeHumidityPercent = 91f,
                    leafWetnessHours = 9.5f,
                    timestamp = now - 3600000,
                    isAnomaly = true
                ),
                SensorReading(
                    fieldName = "Plot 2 - Maize Field",
                    soilMoisturePercent = 48f,
                    canopyTempC = 30.5f,
                    relativeHumidityPercent = 62f,
                    leafWetnessHours = 2.0f,
                    timestamp = now - 3600000 * 2,
                    isAnomaly = false
                ),
                SensorReading(
                    fieldName = "Plot 3 - Rice Wetland",
                    soilMoisturePercent = 96f,
                    canopyTempC = 26.2f,
                    relativeHumidityPercent = 88f,
                    leafWetnessHours = 11.0f,
                    timestamp = now - 3600000 * 3,
                    isAnomaly = true
                )
            )

            sensorLogs.forEach { db.sensorDao().insertReading(it) }

            // Seed Official Alerts
            val alerts = listOf(
                OfficialAlert(
                    title = "District Agricultural Urgent Advisory: Late Blight Surge",
                    broadcastZone = "District Agro-Climatic Zone 4",
                    targetCrops = "Tomato, Potato, Chili",
                    severity = ThreatSeverity.CRITICAL,
                    message = "Continuous overcast weather with relative humidity above 88% has triggered Late Blight hotspots in Khed and Haveli talukas. Extension teams deployed with diagnostic kits.",
                    issuedBy = "District Agriculture Department & State Krishi Vigyan Kendra (KVK)",
                    date = "Today",
                    actionRequired = "Inspect lower leaf canopies. Apply protective Mancozeb or curative Cymoxanil. Submit sample to extension lab if lesions exceed 10%."
                ),
                OfficialAlert(
                    title = "Pheromone Trap Threshold Warning: Armyworm",
                    broadcastZone = "East Valley Agricultural Zone",
                    targetCrops = "Maize, Sweet Corn, Sorghum",
                    severity = ThreatSeverity.HIGH,
                    message = "Surveillance grid reports >18 moths/trap over 48 hours in Maize corridors. Early instar larvae detected in central whorls.",
                    issuedBy = "Plant Protection Directorate",
                    date = "Yesterday",
                    actionRequired = "Deploy bio-agent Trichogramma or whorl-targeted spray of Chlorantraniliprole. Avoid indiscriminate broadcast spraying."
                ),
                OfficialAlert(
                    title = "Safe Pesticide Usage & Maximum Residue Limit (MRL) Notice",
                    broadcastZone = "All Agricultural Blocks",
                    targetCrops = "Export & Domestic Vegetables",
                    severity = ThreatSeverity.LOW,
                    message = "Strict adherence to Pre-Harvest Intervals (PHI) required for upcoming market harvest. Do not spray within 7-14 days of picking.",
                    issuedBy = "Directorate of Plant Quarantine & Residue Control",
                    date = "3 days ago",
                    actionRequired = "Check pesticide labels for exact PHI days and strictly wear personal protective equipment (PPE)."
                )
            )

            db.hotspotDao().insertAllAlerts(alerts)
            db.userProfileDao().insertOrUpdateProfile(
                UserProfile(
                    id = 1L,
                    name = "Farmer",
                    location = "Yavatmal, Vidarbha, Maharashtra",
                    staffUnlocked = false
                )
            )
        }
    }
}
