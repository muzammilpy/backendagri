package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CropHealthRepository
import com.example.model.*
import com.example.util.AppStrings
import com.example.util.ImageUtils
import com.example.util.ImageUtils.toBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

sealed interface DiagnosisUiState {
    object Idle : DiagnosisUiState
    object Analyzing : DiagnosisUiState
    data class Success(val report: CropReport) : DiagnosisUiState
    data class InvalidPlant(val reason: String) : DiagnosisUiState
    data class Error(val message: String) : DiagnosisUiState
}

class CropHealthViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository = CropHealthRepository.getInstance(application, viewModelScope)

    val selectedLanguage = repository.selectedLanguage

    val userProfile = repository.userProfile.map { profile ->
        profile ?: UserProfile(id = 1L, name = "Farmer")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile(id = 1L, name = "Farmer")
    )

    fun updateUserName(name: String) {
        viewModelScope.launch {
            repository.updateUserName(name)
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repository.setLanguage(language)
            updateTtsLanguage(language)
        }
    }

    private fun updateTtsLanguage(language: AppLanguage) {
        val locale = when (language) {
            AppLanguage.ENGLISH -> Locale.ENGLISH
            AppLanguage.HINDI -> Locale("hi", "IN")
            AppLanguage.MARATHI -> Locale("mr", "IN")
            AppLanguage.SPANISH -> Locale("es", "ES")
            AppLanguage.SWAHILI -> Locale("sw", "TZ")
            AppLanguage.BENGALI -> Locale("bn", "IN")
            AppLanguage.FRENCH -> Locale.FRENCH
        }
        try {
            tts?.language = locale
        } catch (e: Exception) {
            // Graceful fallback
        }
    }

    val allReports = repository.allReports.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allTraps = repository.allTraps.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recentSensors = repository.recentSensors.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allHotspots = repository.allHotspots.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allAlerts = repository.allAlerts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val weatherForecasts = repository.liveWeatherForecasts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.getWeatherRiskForecasts()
    )

    val isWeatherSyncing = repository.isWeatherSyncing.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val lastWeatherSyncTimestamp = repository.lastWeatherSyncTimestamp.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = System.currentTimeMillis()
    )

    fun refreshWeatherForecasts(latitude: Double = 20.3888, longitude: Double = 78.1204) {
        viewModelScope.launch {
            repository.syncLiveWeatherFromOpenMeteo(latitude, longitude)
        }
    }

    init {
        // Automatically sync fresh weather forecast on launch
        viewModelScope.launch {
            repository.syncLiveWeatherFromOpenMeteo()
        }

        // Restore language preference from persisted UserProfile
        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile != null && profile.selectedLanguageCode.isNotBlank()) {
                    val lang = AppLanguage.fromCode(profile.selectedLanguageCode)
                    if (repository.selectedLanguage.value != lang) {
                        repository.setLanguageInMemory(lang)
                        updateTtsLanguage(lang)
                    }
                }
            }
        }
    }

    // Weather Risk Telemetry & Threshold Alerts State
    val availableWeatherStations = repository.weatherService.getAvailableStations()

    private val _currentMicroclimate = MutableStateFlow(availableWeatherStations.first())
    val currentMicroclimate = _currentMicroclimate.asStateFlow()

    private val _activeWeatherRiskAlerts = MutableStateFlow(
        repository.weatherService.evaluateRiskThresholds(availableWeatherStations.first())
    )
    val activeWeatherRiskAlerts = _activeWeatherRiskAlerts.asStateFlow()

    private val _isWeatherLoading = MutableStateFlow(false)
    val isWeatherLoading = _isWeatherLoading.asStateFlow()

    fun selectWeatherStation(stationId: String) {
        viewModelScope.launch {
            _isWeatherLoading.value = true
            val result = repository.weatherService.fetchCurrentMicroclimate(stationId)
            result.onSuccess { weather ->
                _currentMicroclimate.value = weather
                _activeWeatherRiskAlerts.value = repository.weatherService.evaluateRiskThresholds(weather)
            }
            _isWeatherLoading.value = false
        }
    }

    fun refreshWeatherData() {
        viewModelScope.launch {
            _isWeatherLoading.value = true
            val currentStationId = _currentMicroclimate.value.stationId
            val result = repository.weatherService.fetchCurrentMicroclimate(currentStationId)
            result.onSuccess { weather ->
                _currentMicroclimate.value = weather
                _activeWeatherRiskAlerts.value = repository.weatherService.evaluateRiskThresholds(weather)
            }
            _isWeatherLoading.value = false
        }
    }

    fun simulateWeatherConditions(temp: Float, humidity: Float, leafWetness: Float, rain: Float) {
        val simulated = repository.weatherService.createSimulatedReading(temp, humidity, leafWetness, rain)
        _currentMicroclimate.value = simulated
        _activeWeatherRiskAlerts.value = repository.weatherService.evaluateRiskThresholds(simulated)
    }

    fun applyWeatherAlertToDiagnosis(alert: WeatherRiskThresholdAlert) {
        val weatherSummary = "${_currentMicroclimate.value.description} (Temp: ${_currentMicroclimate.value.temperatureC}°C, RH: ${_currentMicroclimate.value.relativeHumidityPercent.toInt()}%, Rain: ${_currentMicroclimate.value.rainfallMm}mm)"
        _selectedWeather.value = weatherSummary
        _sensorTemp.value = _currentMicroclimate.value.temperatureC
        _sensorMoisture.value = _currentMicroclimate.value.relativeHumidityPercent

        if (alert.affectedCrops.isNotEmpty()) {
            val matchingCrop = alert.affectedCrops.firstOrNull() ?: "Tomato"
            _selectedCrop.value = matchingCrop
        }
        _symptomDescription.value = "Weather alert trigger: ${alert.title}. ${alert.riskMechanism.take(160)}"
    }

    // Diagnosis Form State
    private val _farmerName = MutableStateFlow("Farmer Anand")
    val farmerName = _farmerName.asStateFlow()

    private val _villageRegion = MutableStateFlow("Haveli Agrarian Block, Zone 3")
    val villageRegion = _villageRegion.asStateFlow()

    private val _selectedCrop = MutableStateFlow("Tomato")
    val selectedCrop = _selectedCrop.asStateFlow()

    private val _selectedVariety = MutableStateFlow("Abhinav F1")
    val selectedVariety = _selectedVariety.asStateFlow()

    private val _selectedStage = MutableStateFlow("Fruiting Stage")
    val selectedStage = _selectedStage.asStateFlow()

    private val _selectedSoil = MutableStateFlow("Loamy (Moist)")
    val selectedSoil = _selectedSoil.asStateFlow()

    private val _selectedWeather = MutableStateFlow("Overcast, High Humidity 88%")
    val selectedWeather = _selectedWeather.asStateFlow()

    private val _symptomDescription = MutableStateFlow("Dark brown water-soaked blotches on leaves with faint white fungal fuzz on lower surface.")
    val symptomDescription = _symptomDescription.asStateFlow()

    private val _trapCount = MutableStateFlow(4)
    val trapCount = _trapCount.asStateFlow()

    private val _sensorMoisture = MutableStateFlow(78f)
    val sensorMoisture = _sensorMoisture.asStateFlow()

    private val _sensorTemp = MutableStateFlow(24.5f)
    val sensorTemp = _sensorTemp.asStateFlow()

    private val _sampleImageKey = MutableStateFlow("tomato_late_blight")
    val sampleImageKey = _sampleImageKey.asStateFlow()

    // Uploaded Custom Crop Leaf Image
    private val _customImageUri = MutableStateFlow<Uri?>(null)
    val customImageUri = _customImageUri.asStateFlow()

    private val _customImageBitmap = MutableStateFlow<Bitmap?>(null)
    val customImageBitmap = _customImageBitmap.asStateFlow()

    private val _customImageBase64 = MutableStateFlow<String?>(null)
    val customImageBase64 = _customImageBase64.asStateFlow()

    private val _isImageProcessing = MutableStateFlow(false)
    val isImageProcessing = _isImageProcessing.asStateFlow()

    private val _diagnosisState = MutableStateFlow<DiagnosisUiState>(DiagnosisUiState.Idle)
    val diagnosisState = _diagnosisState.asStateFlow()

    // Active selected report for detail view
    private val _activeDetailReport = MutableStateFlow<CropReport?>(null)
    val activeDetailReport = _activeDetailReport.asStateFlow()

    // Filter states
    private val _cropFilter = MutableStateFlow("All")
    val cropFilter = _cropFilter.asStateFlow()

    private val _severityFilter = MutableStateFlow<ThreatSeverity?>(null)
    val severityFilter = _severityFilter.asStateFlow()

    // Text To Speech
    private var tts: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking = _isSpeaking.asStateFlow()

    init {
        tts = TextToSpeech(application, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
        }
    }

    fun setCustomImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isImageProcessing.value = true
            _customImageUri.value = uri
            _sampleImageKey.value = "user_uploaded_leaf"
            // Decouple presets: clear hardcoded selections so Gemini Vision model can auto-detect and populate
            _selectedCrop.value = ""
            _selectedVariety.value = ""
            _selectedStage.value = ""
            _symptomDescription.value = "Custom leaf specimen captured. Run AI Diagnosis to analyze lesions and classify crop."

            val bitmap = withContext(Dispatchers.IO) {
                ImageUtils.uriToBitmap(context, uri, maxDimension = 1024)
            }
            _customImageBitmap.value = bitmap
            val base64 = withContext(Dispatchers.IO) {
                bitmap?.toBase64(quality = 85)
            }
            _customImageBase64.value = base64
            _isImageProcessing.value = false
        }
    }

    fun clearCustomImage() {
        _customImageUri.value = null
        _customImageBitmap.value = null
        _customImageBase64.value = null
        _sampleImageKey.value = "tomato_late_blight"
    }

    fun updateForm(
        farmer: String = _farmerName.value,
        village: String = _villageRegion.value,
        crop: String = _selectedCrop.value,
        variety: String = _selectedVariety.value,
        stage: String = _selectedStage.value,
        soil: String = _selectedSoil.value,
        weather: String = _selectedWeather.value,
        symptoms: String = _symptomDescription.value,
        trap: Int = _trapCount.value,
        moisture: Float = _sensorMoisture.value,
        temp: Float = _sensorTemp.value,
        imageKey: String = _sampleImageKey.value
    ) {
        _farmerName.value = farmer
        _villageRegion.value = village
        _selectedCrop.value = crop
        _selectedVariety.value = variety
        _selectedStage.value = stage
        _selectedSoil.value = soil
        _selectedWeather.value = weather
        _symptomDescription.value = symptoms
        _trapCount.value = trap
        _sensorMoisture.value = moisture
        _sensorTemp.value = temp
        _sampleImageKey.value = imageKey
    }

    fun selectPresetSample(
        crop: String,
        variety: String,
        stage: String,
        soil: String,
        weather: String,
        symptoms: String,
        trap: Int,
        moisture: Float,
        temp: Float,
        imageKey: String
    ) {
        _customImageUri.value = null
        _customImageBitmap.value = null
        _customImageBase64.value = null
        updateForm(
            crop = crop,
            variety = variety,
            stage = stage,
            soil = soil,
            weather = weather,
            symptoms = symptoms,
            trap = trap,
            moisture = moisture,
            temp = temp,
            imageKey = imageKey
        )
    }

    fun selectAutoDetectMode() {
        _customImageUri.value = null
        _customImageBitmap.value = null
        _customImageBase64.value = null
        _sampleImageKey.value = "auto_detect"
        _selectedCrop.value = ""
        _selectedVariety.value = ""
        _selectedStage.value = ""
        _symptomDescription.value = ""
    }

    fun runDiagnosis() {
        viewModelScope.launch {
            _diagnosisState.value = DiagnosisUiState.Analyzing
            try {
                val isCustomPhoto = _customImageUri.value != null
                // If using a preset sample without custom image, synthesize a base64 thumbnail if needed
                val imageBase64ToSend = _customImageBase64.value ?: withContext(Dispatchers.IO) {
                    val specimenBitmap = ImageUtils.createSpecimenBitmap(_sampleImageKey.value)
                    specimenBitmap.toBase64(75)
                }

                val (report, aiResponse) = repository.diagnoseAndCreateReport(
                    farmerName = _farmerName.value,
                    villageRegion = _villageRegion.value,
                    crop = _selectedCrop.value,
                    variety = _selectedVariety.value,
                    stage = _selectedStage.value,
                    soil = _selectedSoil.value,
                    weather = _selectedWeather.value,
                    symptoms = _symptomDescription.value,
                    trapCount = _trapCount.value,
                    sensorMoisture = _sensorMoisture.value,
                    sensorTemp = _sensorTemp.value,
                    sampleImageKey = _sampleImageKey.value,
                    imageBase64 = imageBase64ToSend,
                    customImageUri = _customImageUri.value?.toString()
                )

                if (!aiResponse.isValidPlant || report == null) {
                    _diagnosisState.value = DiagnosisUiState.InvalidPlant(aiResponse.rejectionReason)
                    _activeDetailReport.value = null
                    return@launch
                }

                // Dynamically update form fields with AI predictions if custom photo, auto-detect, or unpopulated
                if (isCustomPhoto || _selectedCrop.value.isBlank() || _sampleImageKey.value == "auto_detect") {
                    _selectedCrop.value = aiResponse.cropName
                    _selectedVariety.value = aiResponse.variety
                    _selectedStage.value = aiResponse.growthStage
                    _symptomDescription.value = aiResponse.symptomsSummary
                }

                _diagnosisState.value = DiagnosisUiState.Success(report)
                _activeDetailReport.value = report
            } catch (e: Exception) {
                val errorMsg = if (e is java.io.IOException || e.message?.contains("Network", ignoreCase = true) == true) {
                    AppStrings.networkErrorTapToRetry(selectedLanguage.value)
                } else {
                    e.localizedMessage ?: "Diagnosis failed"
                }
                _diagnosisState.value = DiagnosisUiState.Error(errorMsg)
            }
        }
    }

    fun resetDiagnosisState() {
        _diagnosisState.value = DiagnosisUiState.Idle
    }

    fun resetPhotoAndDiagnosis() {
        clearCustomImage()
        _diagnosisState.value = DiagnosisUiState.Idle
    }

    fun setActiveReport(report: CropReport?) {
        _activeDetailReport.value = report
    }

    fun updateTriage(reportId: Long, status: ReportStatus, notes: String, expertName: String) {
        viewModelScope.launch {
            repository.updateTriage(reportId, status, notes, expertName)
            _activeDetailReport.value?.let { current ->
                if (current.id == reportId) {
                    _activeDetailReport.value = current.copy(
                        status = status,
                        expertNotes = notes,
                        expertName = expertName,
                        validationTimestamp = System.currentTimeMillis()
                    )
                }
            }
        }
    }

    fun updateFollowUp(reportId: Long, day3: Boolean, day7: Boolean, day14: Boolean, notes: String) {
        viewModelScope.launch {
            repository.updateFollowUp(reportId, day3, day7, day14, notes)
            _activeDetailReport.value?.let { current ->
                if (current.id == reportId) {
                    _activeDetailReport.value = current.copy(
                        followUpDay3Checked = day3,
                        followUpDay7Checked = day7,
                        followUpDay14Checked = day14,
                        followUpNotes = notes
                    )
                }
            }
        }
    }

    fun addPestTrapLog(
        type: TrapType,
        pest: String,
        crop: String,
        count: Int,
        threshold: Int,
        notes: String,
        location: String
    ) {
        viewModelScope.launch {
            repository.insertTrapLog(type, pest, crop, count, threshold, notes, location)
        }
    }

    fun addOfficialAlert(
        title: String,
        zone: String,
        crops: String,
        severity: ThreatSeverity,
        message: String,
        issuedBy: String,
        action: String
    ) {
        viewModelScope.launch {
            val alert = OfficialAlert(
                title = title,
                broadcastZone = zone,
                targetCrops = crops,
                severity = severity,
                message = message,
                issuedBy = issuedBy,
                date = "Just Now",
                actionRequired = action
            )
            repository.insertOfficialAlert(alert)
        }
    }

    fun deleteReport(report: CropReport) {
        viewModelScope.launch {
            repository.deleteReport(report)
            if (_activeDetailReport.value?.id == report.id) {
                _activeDetailReport.value = null
            }
        }
    }

    fun setCropFilter(crop: String) {
        _cropFilter.value = crop
    }

    fun setSeverityFilter(severity: ThreatSeverity?) {
        _severityFilter.value = severity
    }

    // Diagnosis History Search and Filter State
    private val _diagnosisSearchQuery = MutableStateFlow("")
    val diagnosisSearchQuery: StateFlow<String> = _diagnosisSearchQuery.asStateFlow()

    private val _diagnosisCropFilter = MutableStateFlow<String?>(null)
    val diagnosisCropFilter: StateFlow<String?> = _diagnosisCropFilter.asStateFlow()

    fun setDiagnosisSearchQuery(query: String) {
        _diagnosisSearchQuery.value = query
    }

    fun setDiagnosisCropFilter(crop: String?) {
        _diagnosisCropFilter.value = crop
    }

    fun clearDiagnosisFilters() {
        _diagnosisSearchQuery.value = ""
        _diagnosisCropFilter.value = null
    }

    // IPM Advisor Decision Tree State
    private val _currentIpmNodeId = MutableStateFlow("root")
    val currentIpmNodeId: StateFlow<String> = _currentIpmNodeId.asStateFlow()

    private val _ipmHistory = MutableStateFlow<List<DecisionTreeHistoryStep>>(emptyList())
    val ipmHistory: StateFlow<List<DecisionTreeHistoryStep>> = _ipmHistory.asStateFlow()

    private val _currentIpmRecommendation = MutableStateFlow<IPMProtocolRecommendation?>(null)
    val currentIpmRecommendation: StateFlow<IPMProtocolRecommendation?> = _currentIpmRecommendation.asStateFlow()

    private val _ipmSearchQuery = MutableStateFlow("")
    val ipmSearchQuery: StateFlow<String> = _ipmSearchQuery.asStateFlow()

    private val _ipmSelectedCropFilter = MutableStateFlow<String?>(null)
    val ipmSelectedCropFilter: StateFlow<String?> = _ipmSelectedCropFilter.asStateFlow()

    fun selectIpmOption(currentNode: DecisionTreeNode, option: DecisionTreeOption) {
        val newHistory = _ipmHistory.value + DecisionTreeHistoryStep(
            nodeId = currentNode.nodeId,
            question = currentNode.question,
            selectedOption = option
        )
        _ipmHistory.value = newHistory

        if (option.finalRecommendationId != null) {
            val rec = com.example.data.local.IPMDecisionTreeData.getRecommendationById(option.finalRecommendationId)
            _currentIpmRecommendation.value = rec
        } else if (option.nextNodeId != null) {
            _currentIpmNodeId.value = option.nextNodeId
            _currentIpmRecommendation.value = null
        }
    }

    fun navigateIpmBack() {
        val history = _ipmHistory.value
        if (history.isNotEmpty()) {
            val lastStep = history.last()
            val newHistory = history.dropLast(1)
            _ipmHistory.value = newHistory
            _currentIpmNodeId.value = lastStep.nodeId
            _currentIpmRecommendation.value = null
        } else {
            _currentIpmNodeId.value = "root"
            _currentIpmRecommendation.value = null
        }
    }

    fun jumpToIpmHistoryStep(index: Int) {
        val history = _ipmHistory.value
        if (index in history.indices) {
            val targetStep = history[index]
            _ipmHistory.value = history.take(index)
            _currentIpmNodeId.value = targetStep.nodeId
            _currentIpmRecommendation.value = null
        }
    }

    fun resetIpmDecisionTree() {
        _currentIpmNodeId.value = "root"
        _ipmHistory.value = emptyList()
        _currentIpmRecommendation.value = null
    }

    fun selectIpmRecommendationDirectly(recommendationId: String) {
        val rec = com.example.data.local.IPMDecisionTreeData.getRecommendationById(recommendationId)
        _currentIpmRecommendation.value = rec
    }

    fun clearIpmRecommendation() {
        _currentIpmRecommendation.value = null
    }

    fun setIpmSearchQuery(query: String) {
        _ipmSearchQuery.value = query
    }

    fun setIpmCropFilter(crop: String?) {
        _ipmSelectedCropFilter.value = crop
    }

    fun applyIpmToDiagnosis(recommendation: IPMProtocolRecommendation) {
        val primaryCrop = recommendation.targetCrops.firstOrNull() ?: "Tomato"
        updateForm(
            crop = primaryCrop,
            symptoms = "Suspected ${recommendation.diseaseName} (${recommendation.scientificName}): ${recommendation.keySymptomsSummary}"
        )
    }

    fun speakAdvisory(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "advisory_tts")
        _isSpeaking.value = true
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }
}
