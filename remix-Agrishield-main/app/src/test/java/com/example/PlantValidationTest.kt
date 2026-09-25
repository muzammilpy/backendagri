package com.example

import com.example.data.api.GeminiCropService
import com.example.model.AIDiagnosisResponse
import com.example.model.AppLanguage
import com.example.util.AppStrings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PlantValidationTest {

    private val service = GeminiCropService()

    @Test
    fun `verify markdown code fence stripping`() {
        val rawWithFences = """
            ```json
            {
              "is_valid_plant": true,
              "crop_name": "Tomato",
              "disease_name": "Early Blight",
              "scientific_name": "Alternaria solani",
              "confidence": 88,
              "severity": "Moderate",
              "symptoms": "Concentric dark brown rings on lower leaves",
              "treatment": "Apply Mancozeb or copper oxychloride"
            }
            ```
        """.trimIndent()

        val cleaned = service.cleanJsonString(rawWithFences)
        assertTrue(cleaned.startsWith("{"))
        assertTrue(cleaned.endsWith("}"))

        val response = service.parseDiagnosisJson(rawWithFences, "Tomato", "Hybrid", "Fruiting", "Lesions")
        assertTrue(response.isValidPlant)
        assertEquals("Tomato", response.cropName)
        assertEquals("Early Blight", response.diseaseName)
        assertEquals(88, response.confidence)
        assertEquals("Moderate", response.severity)
    }

    @Test
    fun `verify genuine crop leaf acceptance when valid`() {
        val genuinePlantJson = """
            {
              "is_valid_plant": true,
              "crop_name": "Cotton",
              "disease_name": "Bacterial Blight",
              "scientific_name": "Xanthomonas citri pv. malvacearum",
              "confidence": 94,
              "severity": "Severe",
              "symptoms": "Angular water-soaked leaf spots turning dark brown",
              "treatment": "Copper oxychloride + Streptocycline"
            }
        """.trimIndent()

        val response = service.parseDiagnosisJson(genuinePlantJson, "Cotton", "Bt Hybrid", "Boll", "Spots")
        assertTrue(response.isValidPlant)
        assertEquals("Cotton", response.cropName)
        assertEquals("Bacterial Blight", response.diseaseName)
        assertEquals("Severe", response.severity)
    }

    @Test
    fun `verify default acceptance if is_valid_plant is omitted`() {
        val standardJson = """
            {
              "crop_name": "Rice",
              "disease_name": "Blast",
              "scientific_name": "Magnaporthe oryzae",
              "confidence": 89,
              "severity": "High",
              "symptoms": "Spindle-shaped lesions with grey center",
              "treatment": "Tricyclazole 75% WP"
            }
        """.trimIndent()

        val response = service.parseDiagnosisJson(standardJson, "Rice", "Basmati", "Tillering", "Lesions")
        assertTrue(response.isValidPlant)
        assertEquals("Rice", response.cropName)
        assertEquals("Blast", response.diseaseName)
    }

    @Test
    fun `verify non-plant rejection parsing`() {
        val nonPlantJson = """
            {
                "is_valid_plant": false,
                "rejection_reason": "No crop or leaf detected. The image appears to contain household furniture.",
                "crop_name": "Invalid Specimen",
                "disease_name": "N/A",
                "scientific_name": "N/A",
                "confidence": 0,
                "severity": "LOW",
                "symptoms": "Non-plant object",
                "treatment": "None"
            }
        """.trimIndent()

        val response = service.parseDiagnosisJson(nonPlantJson, "Tomato", "Hybrid", "Fruiting", "Lesions")
        assertFalse(response.isValidPlant)
        assertTrue(response.rejectionReason.contains("No crop or leaf detected"))
    }

    @Test
    fun `verify strict non-plant error response parsing`() {
        val nonPlantJson = """
            {
                "is_valid_plant": false,
                "error": "Non-plant image detected"
            }
        """.trimIndent()

        val response = service.parseDiagnosisJson(nonPlantJson, "Tomato", "Hybrid", "Fruiting", "Lesions")
        assertFalse(response.isValidPlant)
        assertEquals("Non-plant image detected", response.rejectionReason)
    }

    @Test
    fun `verify localized invalid plant messages`() {
        val enMsg = AppStrings.invalidPlantDiagnosisFailed(AppLanguage.ENGLISH)
        val hiMsg = AppStrings.invalidPlantDiagnosisFailed(AppLanguage.HINDI)
        val mrMsg = AppStrings.invalidPlantDiagnosisFailed(AppLanguage.MARATHI)
        val bnMsg = AppStrings.invalidPlantDiagnosisFailed(AppLanguage.BENGALI)
        val esMsg = AppStrings.invalidPlantDiagnosisFailed(AppLanguage.SPANISH)
        val frMsg = AppStrings.invalidPlantDiagnosisFailed(AppLanguage.FRENCH)
        val swMsg = AppStrings.invalidPlantDiagnosisFailed(AppLanguage.SWAHILI)

        assertTrue(enMsg.contains("Diagnosis Failed: No plant or crop leaf detected"))
        assertTrue(hiMsg.contains("निदान विफल: कोई पौधा या फसल का पत्ता नहीं मिला"))
        assertTrue(mrMsg.contains("तपासणी अयशस्वी: कोणतीही वनस्पती किंवा पिकाचे पान आढळले नाही"))
        assertTrue(bnMsg.contains("শনাক্তকরণ ব্যর্থ হয়েছে"))
        assertTrue(esMsg.contains("Diagnóstico Fallido"))
        assertTrue(frMsg.contains("Échec du Diagnostic"))
        assertTrue(swMsg.contains("Utambuzi Umeshindwa"))
    }

    @Test
    fun `verify localized network error retry messages`() {
        val enRetry = AppStrings.networkErrorTapToRetry(AppLanguage.ENGLISH)
        val hiRetry = AppStrings.networkErrorTapToRetry(AppLanguage.HINDI)
        val mrRetry = AppStrings.networkErrorTapToRetry(AppLanguage.MARATHI)

        assertEquals("Network Error, tap to retry", enRetry)
        assertEquals("नेटवर्क त्रुटि, पुनः प्रयास करने के लिए टैप करें", hiRetry)
        assertEquals("नेटवर्क त्रुटी, पुन्हा प्रयत्न करण्यासाठी टॅप करा", mrRetry)
    }
}
