package com.agrishield.app.domain

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.agrishield.app.data.local.AppDatabase
import com.agrishield.app.data.model.*
import com.agrishield.app.ml.CottonClassifier
import com.agrishield.app.data.remote.GeminiService
import com.agrishield.app.util.LocationHelper
import com.agrishield.app.util.districtForCoordinates
import com.agrishield.app.util.generateReportId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

// ── UI state ─────────────────────────────────────────────────────────────

sealed class DiagnosisState {
    object Idle : DiagnosisState()
    object LoadingModel : DiagnosisState()
    object Classifying : DiagnosisState()
    data class Success(val result: DiagnosisResult) : DiagnosisState()
    data class Error(val message: String) : DiagnosisState()
}

sealed class LocationState {
    object Waiting : LocationState()
    data class Available(val lat: Double, val lon: Double, val name: String) : LocationState()
    data class Error(val message: String) : LocationState()
}

// ── ViewModel ─────────────────────────────────────────────────────────────

class DiagnosisViewModel(application: Application) : AndroidViewModel(application) {

    private val classifier     = CottonClassifier(application)
    private val weatherRepo    = WeatherRepository()
    private val db             = AppDatabase.getInstance(application)
    private val locationHelper = LocationHelper(application)

    private val _diagState   = MutableStateFlow<DiagnosisState>(DiagnosisState.Idle)
    val diagState: StateFlow<DiagnosisState> = _diagState.asStateFlow()

    private val _locationState = MutableStateFlow<LocationState>(LocationState.Waiting)
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _weather = MutableStateFlow<WeatherForecast?>(null)
    val weather: StateFlow<WeatherForecast?> = _weather.asStateFlow()

    val allDiagnoses = db.diagnosisDao().getAllFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var tts: TextToSpeech? = null
    private var currentLat = 19.7515
    private var currentLon = 75.7139

    // ── Init ──────────────────────────────────────────────────────────────

    init {
        initClassifier()
        startLocationUpdates()
    }

    private fun initClassifier() {
        viewModelScope.launch(Dispatchers.IO) {
            _diagState.value = DiagnosisState.LoadingModel
            try {
                classifier.initialize()
                _diagState.value = DiagnosisState.Idle
                Log.i("DiagVM", "Classifier ready")
            } catch (e: Exception) {
                _diagState.value = DiagnosisState.Error(
                    "Model not found. Please run train_model.py and copy " +
                    "agrishield_cotton.tflite into app/src/main/assets/"
                )
            }
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            try {
            locationHelper.getLocationFlow().collect { loc ->
                currentLat = loc.latitude
                currentLon = loc.longitude
                val district = districtForCoordinates(loc.latitude, loc.longitude)
                _locationState.value = LocationState.Available(
                    loc.latitude, loc.longitude,
                    district.ifEmpty { "${loc.latitude.format(3)}, ${loc.longitude.format(3)}" }
                )
                // Refresh weather whenever location changes
                fetchWeather(loc.latitude, loc.longitude)
            }
            } catch (e: Exception) {
                Log.w("DiagVM", "Location updates failed (permissions not granted yet): ${e.message}")
                _locationState.value = LocationState.Available(currentLat, currentLon, "Maharashtra, India")
            }
        }
    }

    // ── Language ─────────────────────────────────────────────────────────

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        tts?.language = Locale.forLanguageTag(lang.code)
    }

    // ── Classify ─────────────────────────────────────────────────────────

    /**
     * Main entry point: takes an image URI from camera/gallery,
     * runs TFLite inference, fetches weather, persists to Room DB.
     */
    fun classify(imageUri: Uri) {
        viewModelScope.launch {
            _diagState.value = DiagnosisState.Classifying
            try {
                // 1. Load bitmap
                val bitmap = withContext(Dispatchers.IO) {
                    loadBitmap(imageUri)
                }

                // 2. Run ML inference on IO thread
                val predictions = withContext(Dispatchers.IO) {
                    classifier.classify(bitmap)
                }

                val top = predictions.first()
                val confBand = when {
                    top.confidence >= 85 -> "High confidence — Strong visual match"
                    top.confidence >= 65 -> "Moderate confidence — Confirm by inspection"
                    else                 -> "Low confidence — Seek expert validation at KVK"
                }

                // 3. Try Gemini Vision for specific disease identification (needs internet)
                val geminiResult = GeminiService.diagnose(
                    bitmap       = bitmap,
                    cropName     = "Auto-Detect",
                    tfliteResult = top.displayName,
                    lang         = _language.value.code
                )

                if (geminiResult != null && !geminiResult.isValidPlant) {
                    _diagState.value = DiagnosisState.Error(
                        geminiResult.rejectionReason.ifBlank {
                            "⚠️ Diagnosis Failed: No plant or crop leaf detected. Please take a clear photo of an infected leaf or field crop specimen."
                        }
                    )
                    return@launch
                }

                // 4. Merge: use Gemini's specific diagnosis if available, TFLite as fallback
                val finalPrediction = if (geminiResult != null && geminiResult.isValidPlant) {
                    top.copy(
                        displayName     = geminiResult.diseaseName,
                        label           = geminiResult.diseaseName,
                        severity        = geminiResult.severity,
                        confidence      = geminiResult.confidence.toFloat(),
                        immediateAction = geminiResult.immediateAction,
                        biological      = geminiResult.biologicalControl,
                        chemical        = geminiResult.chemicalControl,
                        likelyCauses    = geminiResult.preventionTips,
                        marathi         = geminiResult.diseaseName,
                        hindi           = geminiResult.diseaseName
                    )
                } else if (top.confidence < 45.0f) {
                    top.copy(
                        displayName     = "Unrecognized / Low Confidence Specimen",
                        label           = "Uncertain Diagnosis",
                        severity        = Severity.LOW,
                        immediateAction = "The photo could not be matched to a known crop disease with sufficient confidence. Please provide a clear, well-lit close-up photo of a crop leaf or pest lesion.",
                        biological      = "Ensure good lighting and focus on leaf lesions.",
                        chemical        = "Consult local KVK or agronomy expert for physical verification.",
                        marathi         = "अस्पष्ट नमुना / पुन्हा फोटो घ्या",
                        hindi           = "अस्पष्ट नमूना / पुनः फोटो लें"
                    )
                } else {
                    top  // fallback to TFLite result
                }

                val sourceLabel = if (geminiResult != null && geminiResult.isValidPlant)
                    "Gemini 2.0 Flash Vision (${geminiResult.cropName}) + TFLite v1.0"
                else
                    "AgriShield TFLite v1.0 — offline"

                // 5. Fetch weather for this exact farm GPS location
                val currentWeather = _weather.value?.days?.firstOrNull()

                // 6. Build result
                val result = DiagnosisResult(
                    topPrediction  = finalPrediction,
                    allPredictions = predictions,
                    confidenceBand = confBand,
                    farmLat        = currentLat,
                    farmLon        = currentLon,
                    language       = _language.value.code,
                    modelSource    = sourceLabel,
                    weatherRisk    = currentWeather
                )

                // 5. Persist to Room DB (offline-first)
                withContext(Dispatchers.IO) {
                    val detectedCrop = geminiResult?.cropName ?: if (top.confidence >= 45.0f) "Cotton" else "Unknown Crop"
                    persistDiagnosis(result, imageUri, detectedCrop)
                }

                _diagState.value = DiagnosisState.Success(result)

            } catch (e: Exception) {
                Log.e("DiagVM", "Classification failed", e)
                _diagState.value = DiagnosisState.Error("Classification failed: ${e.message}")
            }
        }
    }

    // ── Weather ──────────────────────────────────────────────────────────

    fun fetchWeather(lat: Double = currentLat, lon: Double = currentLon) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.getForecast(lat, lon).onSuccess { forecast ->
                _weather.value = forecast
            }
        }
    }

    // ── Feedback (learning loop) ─────────────────────────────────────────

    fun saveFeedback(diagnosisId: String, outcome: String, notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.diagnosisDao().saveFeedback(diagnosisId, outcome, notes)
            // Trigger sync so feedback reaches server
            SyncWorker.runNow(getApplication())
        }
    }

    // ── TTS voice advisory ───────────────────────────────────────────────

    fun initTts() {
        tts = TextToSpeech(getApplication()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.forLanguageTag(_language.value.code)
            }
        }
    }

    fun speak(text: String) {
        tts?.stop()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AG_ADVISORY")
    }

    fun stopSpeaking() { tts?.stop() }

    // ── Helpers ───────────────────────────────────────────────────────────

    private fun loadBitmap(uri: Uri): Bitmap {
        return getApplication<Application>().contentResolver
            .openInputStream(uri)
            .use { BitmapFactory.decodeStream(it) }
            ?: throw IllegalArgumentException("Could not decode image from $uri")
    }

    private suspend fun persistDiagnosis(result: DiagnosisResult, imageUri: Uri, cropName: String = "Cotton") {
        val id = generateReportId()
        val locState = _locationState.value
        val districtName = if (locState is LocationState.Available) locState.name else "Unknown"

        // Save a copy of the image to internal storage
        val imagePath = saveImageLocally(imageUri, id)

        val entity = DiagnosisEntity(
            id             = id,
            createdAt      = System.currentTimeMillis(),
            farmLat        = result.farmLat,
            farmLon        = result.farmLon,
            districtName   = districtName,
            imagePath      = imagePath,
            cropLabel      = cropName,
            diagnosisLabel = result.topPrediction.label,
            confidence     = result.topPrediction.confidence,
            severity       = result.topPrediction.severity.name,
            immediateAction= result.topPrediction.immediateAction,
            chemical       = result.topPrediction.chemical,
            biological     = result.topPrediction.biological,
            isSyncedToServer = false    // will be synced by WorkManager
        )
        db.diagnosisDao().insert(entity)
        Log.i("DiagVM", "Saved diagnosis $id to local DB. Pending sync.")
    }

    private fun saveImageLocally(uri: Uri, id: String): String {
        return try {
            val dir = File(getApplication<Application>().filesDir, "captures")
            dir.mkdirs()
            val file = File(dir, "$id.jpg")
            getApplication<Application>().contentResolver
                .openInputStream(uri)?.use { input ->
                    FileOutputStream(file).use { output -> input.copyTo(output) }
                }
            file.absolutePath
        } catch (e: Exception) {
            Log.w("DiagVM", "Could not save image: ${e.message}")
            ""
        }
    }

    private fun Double.format(decimals: Int) = "%.${decimals}f".format(this)

    override fun onCleared() {
        super.onCleared()
        classifier.close()
        tts?.shutdown()
    }
}
