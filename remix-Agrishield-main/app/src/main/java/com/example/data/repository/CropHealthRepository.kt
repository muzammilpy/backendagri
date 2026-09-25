package com.example.data.repository

import android.content.Context
import com.example.data.api.GeminiCropService
import com.example.data.api.MockWeatherService
import com.example.data.api.OpenMeteoWeatherService
import com.example.data.local.CropDatabase
import com.example.model.*
import com.example.util.MicroclimateRiskEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CropHealthRepository(
    private val database: CropDatabase,
    private val geminiService: GeminiCropService = GeminiCropService(),
    val weatherService: MockWeatherService = MockWeatherService(),
    private val openMeteoService: OpenMeteoWeatherService = OpenMeteoWeatherService()
) {
    private val _selectedLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val selectedLanguage = _selectedLanguage.asStateFlow()

    // Dynamic Weather Forecast StateFlows
    private val _liveWeatherForecasts = MutableStateFlow<List<WeatherRiskForecast>>(
        openMeteoService.generateDynamicFallbackForecast(MicroclimateRiskEngine.generateDynamicDayLabels(6))
    )
    val liveWeatherForecasts = _liveWeatherForecasts.asStateFlow()

    private val _isWeatherSyncing = MutableStateFlow(false)
    val isWeatherSyncing = _isWeatherSyncing.asStateFlow()

    private val _lastWeatherSyncTimestamp = MutableStateFlow(System.currentTimeMillis())
    val lastWeatherSyncTimestamp = _lastWeatherSyncTimestamp.asStateFlow()

    suspend fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
        database.userProfileDao().updateLanguage(language.code)
    }

    fun setLanguageInMemory(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    /**
     * Synchronizes live weather and disease risk forecasts from Open-Meteo API.
     */
    suspend fun syncLiveWeatherFromOpenMeteo(
        latitude: Double = 20.3888,
        longitude: Double = 78.1204
    ): List<WeatherRiskForecast> {
        _isWeatherSyncing.value = true
        try {
            val result = openMeteoService.fetchLiveForecast(latitude, longitude)
            val forecasts = result.getOrElse {
                openMeteoService.generateDynamicFallbackForecast(MicroclimateRiskEngine.generateDynamicDayLabels(6))
            }
            _liveWeatherForecasts.value = forecasts
            _lastWeatherSyncTimestamp.value = System.currentTimeMillis()
            return forecasts
        } finally {
            _isWeatherSyncing.value = false
        }
    }

    // Reports Flow
    val userProfile: Flow<UserProfile?> = database.userProfileDao().getUserProfile()
    val allReports: Flow<List<CropReport>> = database.cropReportDao().getAllReports()
    val allTraps: Flow<List<PestTrapLog>> = database.pestTrapDao().getAllTrapLogs()
    val recentSensors: Flow<List<SensorReading>> = database.sensorDao().getRecentReadings()
    val allHotspots: Flow<List<HotspotZone>> = database.hotspotDao().getAllHotspots()
    val allAlerts: Flow<List<OfficialAlert>> = database.hotspotDao().getAllAlerts()

    suspend fun updateUserName(name: String) {
        val sanitized = name.trim().ifBlank { "Farmer" }
        val existing = database.userProfileDao().getUserProfileOnce()
        if (existing != null) {
            database.userProfileDao().insertOrUpdateProfile(existing.copy(name = sanitized))
        } else {
            database.userProfileDao().insertOrUpdateProfile(UserProfile(id = 1L, name = sanitized))
        }
    }

    suspend fun setStaffUnlocked(unlocked: Boolean) {
        val roleLabel = if (unlocked) "Extension / Official (demo)" else "Farmer / Farm Owner"
        val existing = database.userProfileDao().getUserProfileOnce()
        if (existing != null) {
            database.userProfileDao().insertOrUpdateProfile(
                existing.copy(staffUnlocked = unlocked, role = roleLabel)
            )
        } else {
            database.userProfileDao().updateStaffUnlock(unlocked, roleLabel)
        }
    }

    suspend fun diagnoseAndCreateReport(
        farmerName: String,
        villageRegion: String,
        crop: String,
        variety: String,
        stage: String,
        soil: String,
        weather: String,
        symptoms: String,
        trapCount: Int,
        sensorMoisture: Float,
        sensorTemp: Float,
        sampleImageKey: String,
        imageBase64: String? = null,
        customImageUri: String? = null,
        latitude: Double = 20.3888,
        longitude: Double = 78.1204
    ): Pair<CropReport?, AIDiagnosisResponse> {
        val diagnosis = geminiService.diagnoseCropHealth(
            crop = crop,
            variety = variety,
            stage = stage,
            soil = soil,
            weather = weather,
            symptoms = symptoms,
            trapCount = trapCount,
            sensorMoisture = sensorMoisture,
            sensorTemp = sensorTemp,
            imageBase64 = imageBase64,
            language = _selectedLanguage.value
        )

        if (!diagnosis.isValidPlant) {
            return Pair(null, diagnosis)
        }

        val severity = when (diagnosis.severity.uppercase()) {
            "CRITICAL", "SEVERE" -> ThreatSeverity.CRITICAL
            "HIGH" -> ThreatSeverity.HIGH
            "MODERATE", "MEDIUM" -> ThreatSeverity.MODERATE
            else -> ThreatSeverity.LOW
        }

        val effectiveCrop = if (crop.isNotBlank()) crop else diagnosis.cropName
        val effectiveVariety = if (variety.isNotBlank()) variety else diagnosis.variety
        val effectiveStage = if (stage.isNotBlank()) stage else diagnosis.growthStage
        val effectiveSymptoms = if (symptoms.isNotBlank() && symptoms != "Custom leaf specimen captured. Run AI Diagnosis to analyze lesions and classify crop.") symptoms else diagnosis.symptomsSummary

        val report = CropReport(
            farmerName = farmerName.ifBlank { "Farmer Unit" },
            villageRegion = villageRegion.ifBlank { "Zone Sector" },
            cropName = effectiveCrop,
            variety = effectiveVariety,
            cropStage = effectiveStage,
            soilCondition = soil,
            weatherFactors = weather,
            trapCount = trapCount,
            sensorMoisture = sensorMoisture,
            sensorTemp = sensorTemp,
            symptomDescription = effectiveSymptoms,
            sampleImageKey = sampleImageKey,
            customImageUri = customImageUri,
            timestamp = System.currentTimeMillis(),
            diseaseName = diagnosis.diseaseName,
            scientificName = diagnosis.scientificName,
            confidence = diagnosis.confidence,
            severity = severity,
            causeSummary = diagnosis.causeSummary,
            weatherRiskCorrelation = diagnosis.weatherRiskCorrelation,
            ipmBiological = diagnosis.ipmBiological,
            ipmCultural = diagnosis.ipmCultural,
            ipmChemical = diagnosis.ipmChemical,
            safeDosage = diagnosis.safeDosage,
            preHarvestIntervalDays = diagnosis.preHarvestIntervalDays,
            safetyPpeInstructions = diagnosis.safetyPpeInstructions,
            labReferralRecommended = diagnosis.labReferralRecommended,
            status = ReportStatus.AI_DIAGNOSED,
            latitude = latitude,
            longitude = longitude,
            diagnosisSource = diagnosis.diagnosisSource,
            imageWasAnalyzed = diagnosis.imageWasAnalyzed,
            fieldOutcome = FieldOutcome.NONE.name,
            isReferenceCase = false
        )

        val newId = database.cropReportDao().insertReport(report)
        return Pair(report.copy(id = newId), diagnosis)
    }

    suspend fun updateTriage(
        reportId: Long,
        status: ReportStatus,
        notes: String,
        expertName: String
    ) {
        database.cropReportDao().updateExpertTriage(
            id = reportId,
            status = status,
            notes = notes,
            expertName = expertName,
            time = System.currentTimeMillis()
        )
    }

    suspend fun updateFollowUp(
        reportId: Long,
        day3: Boolean,
        day7: Boolean,
        day14: Boolean,
        notes: String
    ) {
        database.cropReportDao().updateFollowUp(reportId, day3, day7, day14, notes)
    }

    suspend fun updateFieldOutcome(reportId: Long, outcome: FieldOutcome) {
        database.cropReportDao().updateFieldOutcome(reportId, outcome.name)
    }

    suspend fun insertTrapLog(
        trapType: TrapType,
        targetPest: String,
        cropField: String,
        count: Int,
        threshold: Int,
        notes: String,
        locationName: String
    ) {
        val exceeded = count >= threshold
        val log = PestTrapLog(
            trapType = trapType,
            targetPest = targetPest,
            cropField = cropField,
            count = count,
            economicThresholdLimit = threshold,
            thresholdExceeded = exceeded,
            notes = notes,
            locationName = locationName,
            timestamp = System.currentTimeMillis()
        )
        database.pestTrapDao().insertTrapLog(log)
    }

    suspend fun insertSensorReading(reading: SensorReading) {
        database.sensorDao().insertReading(reading)
    }

    suspend fun insertOfficialAlert(alert: OfficialAlert) {
        database.hotspotDao().insertAlert(alert)
    }

    suspend fun deleteReport(report: CropReport) {
        database.cropReportDao().deleteReport(report)
    }

    fun getWeatherRiskForecasts(): List<WeatherRiskForecast> {
        val current = _liveWeatherForecasts.value
        return if (current.isNotEmpty()) {
            current
        } else {
            openMeteoService.generateDynamicFallbackForecast(MicroclimateRiskEngine.generateDynamicDayLabels(6))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CropHealthRepository? = null

        fun getInstance(context: Context, scope: CoroutineScope): CropHealthRepository {
            return INSTANCE ?: synchronized(this) {
                val db = CropDatabase.getDatabase(context, scope)
                val instance = CropHealthRepository(db)
                INSTANCE = instance
                instance
            }
        }
    }
}
