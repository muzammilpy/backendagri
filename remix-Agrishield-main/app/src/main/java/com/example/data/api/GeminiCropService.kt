package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.model.AIDiagnosisResponse
import com.example.model.AppLanguage
import com.example.model.ThreatSeverity
import com.example.util.AgriShieldConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiRestApi {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body body: okhttp3.RequestBody
    ): ResponseBody
}

class GeminiCropService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
        .build()

    private val api = retrofit.create(GeminiRestApi::class.java)

    suspend fun diagnoseCropHealth(
        crop: String,
        variety: String,
        stage: String,
        soil: String,
        weather: String,
        symptoms: String,
        trapCount: Int,
        sensorMoisture: Float,
        sensorTemp: Float,
        imageBase64: String? = null,
        language: AppLanguage = AppLanguage.ENGLISH
    ): AIDiagnosisResponse = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val hasCustomImage = !imageBase64.isNullOrBlank()

        if (apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY") {
            val prompt = buildDiagnosisPrompt(
                crop, variety, stage, soil, weather, symptoms, trapCount, sensorMoisture, sensorTemp, hasCustomImage, language
            )
            val partsArray = JSONArray().apply {
                put(JSONObject().put("text", prompt))
                if (hasCustomImage) {
                    put(JSONObject().apply {
                        put("inlineData", JSONObject().apply {
                            put("mimeType", "image/jpeg")
                            put("data", imageBase64)
                        })
                    })
                }
            }

            val jsonPayload = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", partsArray)
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("responseMimeType", "application/json")
                })
            }

            val candidateModels = listOf("gemini-2.5-flash", "gemini-flash-latest", "gemini-3.5-flash")
            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())

            for (model in candidateModels) {
                try {
                    val response = api.generateContent(model, apiKey, requestBody)
                    val rawResponse = response.string()
                    val jsonRoot = JSONObject(rawResponse)
                    val candidates = jsonRoot.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text", "")
                            if (text.isNotBlank()) {
                                return@withContext parseDiagnosisJson(text, crop, variety, stage, symptoms)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.w("GeminiCropService", "Model $model inference failed: ${e.message}, trying fallback model...")
                }
            }
        }

        // Separate network/API timeouts from plant validation:
        // If the API call fails or times out on custom images, throw an IOException so ViewModel shows "Network Error, tap to retry"
        // instead of "Diagnosis Failed: Invalid Specimen".
        // Only show "Diagnosis Failed: Invalid Specimen" when is_valid_plant is explicitly returned as false.
        val apiWasConfigured = apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY"
        if (hasCustomImage && apiWasConfigured) {
            throw java.io.IOException("Network Error, tap to retry")
        }

        fallbackAgronomyEngine(crop, variety, stage, soil, weather, symptoms, trapCount, sensorMoisture, sensorTemp, language)
            .copy(
                diagnosisSource = AgriShieldConfig.RULE_ENGINE_SOURCE,
                imageWasAnalyzed = false
            )
    }

    private fun buildDiagnosisPrompt(
        crop: String,
        variety: String,
        stage: String,
        soil: String,
        weather: String,
        symptoms: String,
        trapCount: Int,
        sensorMoisture: Float,
        sensorTemp: Float,
        hasImage: Boolean,
        language: AppLanguage
    ): String {
        val isAutoDetect = crop.isBlank() || crop.equals("Auto-Detect", ignoreCase = true)
        return """
            You are an agricultural plant pathologist supporting Maharashtra farmers (SIH PS 26131).
            Prefer cultural and biological IPM first. Chemicals last, only CIBRC-labelled actives, with PHI.
            Never claim certainty. This is a suggestion for scouting, not a prescription.
            Analyze this image.
            Determine if the image contains any plant, leaf, crop, fruit, stem, field vegetation, agricultural pest, or plant lesion (even if blurry, zoomed in, or diseased).
            ${if (isAutoDetect) "AUTO-DETECT MODE: Bypassing predefined benchmarks. Automatically identify and classify both the exact crop species and the disease/pathogen strictly from the visual image." else ""}
            Only reject the image if it is unequivocally non-agricultural (e.g., people, furniture, vehicles, indoor household electronics, paper documents, architecture diagrams).
            Return clean JSON (no markdown formatting):
            {
              "is_valid_plant": true/false,
              "crop_name": "identified or closest crop name",
              "disease_name": "identified disease/pest or 'Healthy'",
              "scientific_name": "scientific pathogen name or 'None'",
              "confidence": 85,
              "severity": "Low" | "Moderate" | "Severe",
              "symptoms": "brief description of observed lesions or pest signs",
              "treatment": "recommended IPM treatment / bio-spray",
              "variety": "Detected or standard field variety",
              "growthStage": "Growth stage from foliage",
              "causeSummary": "Epidemiology and pathogen mechanism",
              "weatherRiskCorrelation": "How microclimate weather triggered this",
              "ipmBiological": "Bio-control agents, biopesticides",
              "ipmCultural": "Field sanitation, spacing, pruning",
              "ipmChemical": "Chemical active ingredient if necessary",
              "safeDosage": "Safe dosage per Litre and per Acre",
              "preHarvestIntervalDays": 7,
              "safetyPpeInstructions": "PPE instructions and spray timing",
              "labReferralRecommended": false,
              "followUpAdvice": "Guidance for Day 3, 7, and 14 inspections"
            }

            Context provided by farmer (if any):
            - Crop (if specified): ${if (isAutoDetect) "Auto-detect species directly from image (no crop constraints)" else crop} (${variety.ifBlank { "Auto-detect" }})
            - Growth Stage: ${stage.ifBlank { "Estimate from foliage" }}
            - Soil Condition: ${soil.ifBlank { "Loamy field soil" }} (Moisture: ${sensorMoisture}%)
            - Microclimate / Weather: ${weather.ifBlank { "Field ambient" }} (Canopy Temp: ${sensorTemp}°C)
            - Pest Trap Count / Day: $trapCount
            - Observed Visual Symptoms: ${symptoms.ifBlank { "Detect from visual leaf image" }}
            - Preferred Output Language: ${language.displayName} (${language.nativeName})
        """.trimIndent()
    }

    fun cleanJsonString(raw: String): String {
        var clean = raw.trim()
        if (clean.startsWith("```json", ignoreCase = true)) {
            clean = clean.substring(7)
        } else if (clean.startsWith("```")) {
            clean = clean.substring(3)
        }
        if (clean.endsWith("```")) {
            clean = clean.substring(0, clean.length - 3)
        }
        clean = clean.trim()
        val firstBrace = clean.indexOf('{')
        val lastBrace = clean.lastIndexOf('}')
        if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
            clean = clean.substring(firstBrace, lastBrace + 1).trim()
        }
        return clean
    }

    fun parseDiagnosisJson(
        rawJson: String,
        defaultCrop: String,
        defaultVariety: String,
        defaultStage: String,
        defaultSymptoms: String
    ): AIDiagnosisResponse {
        val clean = cleanJsonString(rawJson)
        val json = JSONObject(clean)

        // Only show "Diagnosis Failed: Invalid Specimen" when is_valid_plant is explicitly returned as false
        val isValidPlant = when {
            json.has("is_valid_plant") -> json.optBoolean("is_valid_plant", true)
            json.has("isValidPlant") -> json.optBoolean("isValidPlant", true)
            else -> true
        }

        val errorText = json.optString("error", "")
        val rejectionReason = if (errorText.isNotBlank()) {
            errorText
        } else {
            json.optString("rejection_reason",
                json.optString("rejectionReason", "⚠️ Diagnosis Failed: No plant or crop leaf detected. Please take a clear photo of an infected leaf or field specimen.")
            )
        }

        if (!isValidPlant) {
            return AIDiagnosisResponse(
                isValidPlant = false,
                rejectionReason = rejectionReason.ifBlank {
                    "⚠️ Diagnosis Failed: No plant or crop leaf detected. Please take a clear photo of an infected leaf or field specimen."
                },
                cropName = "Invalid Specimen",
                variety = "N/A",
                growthStage = "N/A",
                symptomsSummary = rejectionReason,
                diseaseName = "Invalid Specimen",
                scientificName = "Non-botanical object",
                confidence = 0,
                severity = "LOW"
            )
        }

        val fallbackCrop = if (defaultCrop.isNotBlank()) defaultCrop else "Cotton"
        val fallbackVariety = if (defaultVariety.isNotBlank()) defaultVariety else "Bt Hybrid"
        val fallbackStage = if (defaultStage.isNotBlank()) defaultStage else "Foliar Stage"
        val fallbackSymptoms = if (defaultSymptoms.isNotBlank()) defaultSymptoms else "Visual foliar lesions detected by vision model."

        val cropName = json.optString("crop_name", json.optString("cropName", fallbackCrop)).ifBlank { fallbackCrop }
        val variety = json.optString("variety", fallbackVariety).ifBlank { fallbackVariety }
        val growthStage = json.optString("growthStage", fallbackStage).ifBlank { fallbackStage }
        val symptoms = json.optString("symptoms", json.optString("symptomsSummary", fallbackSymptoms)).ifBlank { fallbackSymptoms }
        val diseaseName = json.optString("disease_name", json.optString("diseaseName", "Crop Infection")).ifBlank { "Crop Infection" }
        val scientificName = json.optString("scientific_name", json.optString("scientificName", "Pathogen sp.")).ifBlank { "Pathogen sp." }
        val confidence = json.optInt("confidence", 85).coerceIn(50, 99)
        val severity = json.optString("severity", "Moderate")
        val treatment = json.optString("treatment", "")

        val defaultBio = if (treatment.isNotBlank()) treatment else "Apply bio-control agents like Trichoderma or Bacillus subtilis."
        val defaultChemical = if (treatment.isNotBlank()) treatment else "Targeted application of recommended fungicide/insecticide."

        return AIDiagnosisResponse(
            isValidPlant = true,
            rejectionReason = "",
            cropName = cropName,
            variety = variety,
            growthStage = growthStage,
            symptomsSummary = symptoms,
            diseaseName = diseaseName,
            scientificName = scientificName,
            confidence = confidence,
            severity = severity,
            causeSummary = json.optString("causeSummary", "Identified based on visual lesion analysis and microclimate telemetry."),
            weatherRiskCorrelation = json.optString("weatherRiskCorrelation", "Microclimate humidity and canopy temperature favored disease propagation."),
            ipmBiological = json.optString("ipmBiological", defaultBio),
            ipmCultural = json.optString("ipmCultural", "Prune infected parts and maintain clean row spacing for aeration."),
            ipmChemical = json.optString("ipmChemical", defaultChemical),
            safeDosage = json.optString("safeDosage", "2.0 g/L or 1.5 ml/L of water"),
            preHarvestIntervalDays = json.optInt("preHarvestIntervalDays", 7),
            safetyPpeInstructions = json.optString("safetyPpeInstructions", "Wear rubber gloves, eye goggles, and mask during spray."),
            labReferralRecommended = json.optBoolean("labReferralRecommended", true),
            followUpAdvice = json.optString("followUpAdvice", "Inspect crop canopy every 3-5 days to confirm containment."),
            diagnosisSource = AgriShieldConfig.GEMINI_SOURCE,
            imageWasAnalyzed = true
        )
    }

    fun fallbackAgronomyEngine(
        crop: String,
        variety: String,
        stage: String,
        soil: String,
        weather: String,
        symptoms: String,
        trapCount: Int,
        sensorMoisture: Float,
        sensorTemp: Float,
        language: AppLanguage
    ): AIDiagnosisResponse {
        val symp = symptoms.lowercase()
        val crp = crop.lowercase()

        return when {
            crp.contains("tomato") || crp.contains("potato") -> {
                if (symp.contains("whitefly") || symp.contains("curl") || symp.contains("yellow")) {
                    AIDiagnosisResponse(
                        cropName = if (crop.isNotBlank()) crop else "Tomato",
                        variety = if (variety.isNotBlank()) variety else "Abhinav F1",
                        growthStage = if (stage.isNotBlank()) stage else "Vegetative Stage",
                        symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Severe upward leaf curling and chlorosis from whitefly vectors.",
                        diseaseName = "Tomato Leaf Curl Virus (ToLCV)",
                        scientificName = "Begomovirus (transmitted by Bemisia tabaci)",
                        confidence = 93,
                        severity = "HIGH",
                        causeSummary = "Geminivirus transmitted systemically by whitefly feeding, causing severe stunting and upward leaf curling.",
                        weatherRiskCorrelation = "Warm dry conditions (28-34°C) dramatically accelerate whitefly reproduction and transmission cycles.",
                        ipmBiological = "Install yellow sticky traps (15-20 per acre); spray 5% Neem Seed Kernel Extract (NSKE) or Verticillium lecanii (5g/L).",
                        ipmCultural = "Rogue out early infected viral plants; use barrier crops of maize or sorghum (3 border rows); remove solanaceous weeds.",
                        ipmChemical = "Targeted vector control: Acetamiprid 20% SP @ 0.3g/L or Diafenthiuron 50% WP @ 1.2g/L.",
                        safeDosage = "0.3 g per Litre (60 g / Acre in 200 L water)",
                        preHarvestIntervalDays = 7,
                        safetyPpeInstructions = "Wear protective chemical mask and gloves. Spray during calm early morning hours before bees forage.",
                        labReferralRecommended = true,
                        followUpAdvice = "Monitor sticky card counts at Day 3 and Day 7. Verify new leaf flushes are symptom-free."
                    )
                } else {
                    AIDiagnosisResponse(
                        cropName = if (crop.isNotBlank()) crop else "Tomato",
                        variety = if (variety.isNotBlank()) variety else "Abhinav F1",
                        growthStage = if (stage.isNotBlank()) stage else "Fruiting Stage",
                        symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Dark water-soaked brown lesions on leaves and fruit with white fungal downy growth.",
                        diseaseName = "Late Blight (Phytophthora infestans)",
                        scientificName = "Phytophthora infestans",
                        confidence = 95,
                        severity = if (sensorMoisture > 75f) "CRITICAL" else "HIGH",
                        causeSummary = "Aggressive oomycete attacking foliar canopy, stems, and fruit flesh under damp, cool-to-moderate temperatures.",
                        weatherRiskCorrelation = "Relative humidity > 85% and leaf wetness > 6 hours create the perfect window for zoospore germination.",
                        ipmBiological = "Preventative bio-fungicide: Trichoderma harzianum (5g/L) root drench + Bacillus amyloliquefaciens foliar spray.",
                        ipmCultural = "Ensure adequate row spacing; stake indeterminate tomato varieties; remove bottom 30cm of leaves touching wet soil.",
                        ipmChemical = "Curative systemic: Cymoxanil 8% + Mancozeb 64% WP @ 2.5 g/L or Dimethomorph 50% WP @ 1.0 g/L.",
                        safeDosage = "2.5 g per Litre (500 g / Acre in 200 L water)",
                        preHarvestIntervalDays = 7,
                        safetyPpeInstructions = "Full PPE required: rubber apron, chemical gloves, eye shield. Do not spray near open water sources.",
                        labReferralRecommended = true,
                        followUpAdvice = "Re-examine undersides of leaves at Day 3. If lesion margins remain active, apply second rotational systemic."
                    )
                }
            }
            crp.contains("rice") || crp.contains("paddy") -> {
                if (symp.contains("hopper") || symp.contains("burn") || trapCount > 20) {
                    AIDiagnosisResponse(
                        cropName = if (crop.isNotBlank()) crop else "Rice (Paddy)",
                        variety = if (variety.isNotBlank()) variety else "Swarna Sub-1",
                        growthStage = if (stage.isNotBlank()) stage else "Tillering Stage",
                        symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Circular hopper burn patches with phloem sap-sucking nymphs at base.",
                        diseaseName = "Brown Planthopper (Nilaparvata lugens)",
                        scientificName = "Nilaparvata lugens",
                        confidence = 92,
                        severity = "CRITICAL",
                        causeSummary = "Phloem-sap sucking hoppers congregating at the base of rice tillers, causing catastrophic circular 'hopper burn'.",
                        weatherRiskCorrelation = "Dense tillering canopy with continuous water standing creates a humid microclimate enabling rapid nymphal breeding.",
                        ipmBiological = "Conserve natural predators (Mirid bugs, wolf spiders); apply Metarhizium anisopliae @ 2.5 kg/ha.",
                        ipmCultural = "Alternate Wetting and Drying (AWD) irrigation; create 30cm alleyways every 2-3 meters for aeration and solar penetration.",
                        ipmChemical = "Targeted base spray: Pymetrozine 50% WDG @ 0.6 g/L or Triflumezopyrim 10% SC @ 0.5 ml/L directed at stem base.",
                        safeDosage = "0.6 g per Litre (120 g / Acre targeted at hill base)",
                        preHarvestIntervalDays = 14,
                        safetyPpeInstructions = "Wear protective wading boots, gloves and face mask. Avoid applying organophosphates which induce hopper resurgence.",
                        labReferralRecommended = true,
                        followUpAdvice = "Part the rice canopy at 20 spots to count nymphs per hill on Day 3 and Day 7."
                    )
                } else {
                    AIDiagnosisResponse(
                        cropName = if (crop.isNotBlank()) crop else "Rice (Paddy)",
                        variety = if (variety.isNotBlank()) variety else "Swarna Sub-1",
                        growthStage = if (stage.isNotBlank()) stage else "Panicle Initiation",
                        symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Spindle-shaped diamond eye lesions with grey centers and dark reddish-brown borders.",
                        diseaseName = "Rice Blast (Magnaporthe oryzae)",
                        scientificName = "Magnaporthe oryzae",
                        confidence = 94,
                        severity = "HIGH",
                        causeSummary = "Airborne fungal conidia penetrating leaf cuticle, forming characteristic spindle-shaped diamond lesions with ash-grey centers.",
                        weatherRiskCorrelation = "Extended dew periods (>8 hrs), night temperatures 20-25°C, and excessive chemical nitrogen favor infection.",
                        ipmBiological = "Pseudomonas fluorescens 0.2% foliar spray + silica soil amendment to strengthen leaf epidermal walls.",
                        ipmCultural = "Split nitrogen fertilizer into 3-4 doses with balanced muriate of potash (MOP); avoid drought-induced water stress.",
                        ipmChemical = "Tricyclazole 75% WP @ 0.6 g/L or Isoprothiolane 40% EC @ 1.5 ml/L.",
                        safeDosage = "0.6 g per Litre (120 g in 200 L water per acre)",
                        preHarvestIntervalDays = 21,
                        safetyPpeInstructions = "Wear protective gloves and long sleeves. Wash thoroughly after handling.",
                        labReferralRecommended = false,
                        followUpAdvice = "Inspect panicle necks during heading stage (Day 7 and 14) to prevent devastating neck blast."
                    )
                }
            }
            crp.contains("maize") || crp.contains("corn") -> {
                AIDiagnosisResponse(
                    cropName = if (crop.isNotBlank()) crop else "Maize (Corn)",
                    variety = if (variety.isNotBlank()) variety else "DKC 9108",
                    growthStage = if (stage.isNotBlank()) stage else "Whorl / Vegetative",
                    symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Window-pane feeding holes in whorl leaves with sawdust-like frass.",
                    diseaseName = "Fall Armyworm (Spodoptera frugiperda)",
                    scientificName = "Spodoptera frugiperda",
                    confidence = 96,
                    severity = if (trapCount > 10) "CRITICAL" else "HIGH",
                    causeSummary = "Voracious invasive caterpillar devouring central whorl tissues and cob tips, producing moist frass plug.",
                    weatherRiskCorrelation = "Elevated ambient temperatures (>28°C) shorten the lifecycle to under 25 days, creating overlapping broods.",
                    ipmBiological = "Release egg parasitoid Trichogramma pretiosum (50,000/acre); spray Bacillus thuringiensis (Bt) aizawai @ 2g/L.",
                    ipmCultural = "Hand-pick and crush egg masses; apply neem cake in soil at sowing; install 5 pheromone traps per acre for early alerts.",
                    ipmChemical = "Chlorantraniliprole 18.5% SC @ 0.4 ml/L or Spinetoram 11.7% SC @ 0.5 ml/L directed into central leaf whorls.",
                    safeDosage = "0.4 ml per Litre (80 ml / Acre in 200 L water)",
                    preHarvestIntervalDays = 14,
                    safetyPpeInstructions = "Calibrate nozzle to coarse droplet spray directly into the whorl cup. Wear eye protection and gloves.",
                    labReferralRecommended = false,
                    followUpAdvice = "Check 20 consecutive plants across 5 field zones on Day 3 and Day 7 for fresh frass."
                )
            }
            crp.contains("cotton") || (crp.isBlank() && (symp.contains("boll") || symp.contains("rosett") || symp.contains("aphid") || symp.contains("thrip") || trapCount > 4)) -> {
                AIDiagnosisResponse(
                    cropName = if (crop.isNotBlank()) crop else "Cotton",
                    variety = if (variety.isNotBlank()) variety else "Bt RCH-2",
                    growthStage = if (stage.isNotBlank()) stage else "Squaring & Boll Formation",
                    symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Rosetted flowers, tender green bolls with entry boreholes and lint staining.",
                    diseaseName = "Pink Bollworm (Pectinophora gossypiella)",
                    scientificName = "Pectinophora gossypiella",
                    confidence = 91,
                    severity = if (trapCount > 8) "CRITICAL" else "MODERATE",
                    causeSummary = "Larvae bore into tender flower buds and squares (forming 'rosetted flowers') and tunnel into developing bolls, destroying lint.",
                    weatherRiskCorrelation = "Warm humid evenings trigger peak moth emergence and oviposition in terminal shoots.",
                    ipmBiological = "Install Gossyplure pheromone traps (8/acre) for mating disruption; release Trichogramma bactrae @ 60,000/acre.",
                    ipmCultural = "Destroy cotton crop residues after harvest; avoid ratoon cropping; monitor rosetted flowers and destroy manually.",
                    ipmChemical = "Emamectin benzoate 5% SG @ 0.5 g/L or Profenofos 50% EC @ 2 ml/L at ETL threshold (8 moths/trap/day).",
                    safeDosage = "0.5 g per Litre (100 g / Acre in 200 L water)",
                    preHarvestIntervalDays = 20,
                    safetyPpeInstructions = "Full protective clothing, face shield and gloves. Do not allow grazing animals into sprayed fields for 14 days.",
                    labReferralRecommended = true,
                    followUpAdvice = "Dissect 20 randomly selected bolls at Day 7 to check for internal larval penetration."
                )
            }
            crp.contains("wheat") -> {
                AIDiagnosisResponse(
                    cropName = if (crop.isNotBlank()) crop else "Wheat",
                    variety = if (variety.isNotBlank()) variety else "HD-2967",
                    growthStage = if (stage.isNotBlank()) stage else "Tillering to Boot Stage",
                    symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Linear stripes of bright yellow powdery pustules arranged in parallel lines.",
                    diseaseName = "Yellow Stripe Rust (Puccinia striiformis)",
                    scientificName = "Puccinia striiformis f. sp. tritici",
                    confidence = 93,
                    severity = "HIGH",
                    causeSummary = "Fungal urediniospores forming bright yellow pustules arranged in linear stripes along the leaf veins.",
                    weatherRiskCorrelation = "Cool humid weather (10-18°C) with morning fog and wind currents carries rust spores across agricultural basins.",
                    ipmBiological = "Seed treatment with Trichoderma viride (4g/kg seed); foliar bio-stimulant kelp extracts.",
                    ipmCultural = "Sow stripe-rust resistant varieties; avoid late sowing; maintain balanced phosphate and potash fertilization.",
                    ipmChemical = "Propiconazole 25% EC @ 1.0 ml/L or Tebuconazole 25.9% EC @ 1.0 ml/L at first appearance of yellow stripes.",
                    safeDosage = "1.0 ml per Litre (200 ml / Acre in 200 L water)",
                    preHarvestIntervalDays = 30,
                    safetyPpeInstructions = "Wear protective chemical splash goggles, mask, and rubber boots. Spray during low wind conditions.",
                    labReferralRecommended = true,
                    followUpAdvice = "Scout wheat rows diagonally on Day 5 and Day 10 to confirm rust pustules have turned dark and stopped spreading."
                )
            }
            else -> {
                AIDiagnosisResponse(
                    cropName = if (crop.isNotBlank()) crop else "Cotton",
                    variety = if (variety.isNotBlank()) variety else "Desi / Hybrid",
                    growthStage = if (stage.isNotBlank()) stage else "Vegetative / Squaring",
                    symptomsSummary = if (symptoms.isNotBlank()) symptoms else "Foliar chlorotic spotting and margin necrotic lesions detected on specimen.",
                    diseaseName = "Foliar Leaf Spot & Blight Complex",
                    scientificName = "Alternaria / Cercospora fungal complex",
                    confidence = 89,
                    severity = "MODERATE",
                    causeSummary = "Concentric target-like necrotic rings on leaves caused by opportunist fungal pathogens taking advantage of plant stress.",
                    weatherRiskCorrelation = "Intermittent rainfall and alternating wet/dry cycles promote spore detachment and penetration.",
                    ipmBiological = "Spray Copper Hydroxide (2g/L) or bio-agent Pseudomonas fluorescens (5g/L).",
                    ipmCultural = "Remove severely spotted lower leaves; improve soil drainage; mulch soil surface to stop rain-splash spore transmission.",
                    ipmChemical = "Azoxystrobin 18.2% + Difenoconazole 11.4% SC @ 1.0 ml/L or Mancozeb 75% WP @ 2.5 g/L.",
                    safeDosage = "1.0 ml per Litre (200 ml / Acre in 200 L water)",
                    preHarvestIntervalDays = 7,
                    safetyPpeInstructions = "Standard PPE: nitrile gloves, eye protection, and dust/mist respirator mask.",
                    labReferralRecommended = false,
                    followUpAdvice = "Monitor newly emerging shoots on Day 7 to verify clean healthy foliage."
                )
            }
        }
    }
}
