package com.example

import com.example.data.api.GeminiCropService
import com.example.model.AppLanguage
import com.example.model.ThreatSeverity
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    private val agronomyService = GeminiCropService()

    @Test
    fun testTomatoLateBlightDiagnosis() {
        val result = agronomyService.fallbackAgronomyEngine(
            crop = "Tomato",
            variety = "Abhinav F1",
            stage = "Fruiting",
            soil = "Loamy",
            weather = "Overcast, 90% humidity",
            symptoms = "Dark water-soaked brown lesions on leaves and fruit with white fungal downy growth on undersides.",
            trapCount = 4,
            sensorMoisture = 85f,
            sensorTemp = 24.0f,
            language = AppLanguage.ENGLISH
        )

        assertEquals("Late Blight (Phytophthora infestans)", result.diseaseName)
        assertEquals("CRITICAL", result.severity)
        assertTrue(result.ipmBiological.contains("Trichoderma"))
        assertTrue(result.ipmChemical.contains("Mancozeb") || result.ipmChemical.contains("Cymoxanil"))
        assertEquals(7, result.preHarvestIntervalDays)
        assertTrue(result.labReferralRecommended)
    }

    @Test
    fun testMaizeFallArmywormDiagnosis() {
        val result = agronomyService.fallbackAgronomyEngine(
            crop = "Maize",
            variety = "DKC 9108",
            stage = "Whorl",
            soil = "Sandy Loam",
            weather = "Warm, 31°C",
            symptoms = "Window-pane feeding on leaves with sawdust-like frass inside the whorl funnel.",
            trapCount = 18,
            sensorMoisture = 40f,
            sensorTemp = 31.0f,
            language = AppLanguage.ENGLISH
        )

        assertEquals("Fall Armyworm (Spodoptera frugiperda)", result.diseaseName)
        assertEquals("CRITICAL", result.severity)
        assertTrue(result.ipmBiological.contains("Trichogramma") || result.ipmBiological.contains("Bacillus thuringiensis"))
        assertEquals(14, result.preHarvestIntervalDays)
    }

    @Test
    fun testRiceBlastDiagnosis() {
        val result = agronomyService.fallbackAgronomyEngine(
            crop = "Rice (Paddy)",
            variety = "Swarna",
            stage = "Tillering",
            soil = "Clay",
            weather = "Humid 86%",
            symptoms = "Spindle-shaped diamond lesions with grey centers on leaf blades.",
            trapCount = 2,
            sensorMoisture = 95f,
            sensorTemp = 27.0f,
            language = AppLanguage.ENGLISH
        )

        assertEquals("Rice Blast (Magnaporthe oryzae)", result.diseaseName)
        assertEquals("HIGH", result.severity)
        assertTrue(result.ipmChemical.contains("Tricyclazole"))
    }

    @Test
    fun testComputeInitials() {
        assertEquals("F", com.example.model.computeInitials("Farmer"))
        assertEquals("JD", com.example.model.computeInitials("John Doe"))
        assertEquals("DA", com.example.model.computeInitials("Dr. Anita Rao"))
        assertEquals("AN", com.example.model.computeInitials("Anand"))
        assertEquals("A", com.example.model.computeInitials("A"))
        assertEquals("F", com.example.model.computeInitials(""))
        assertEquals("F", com.example.model.computeInitials("   "))
        assertEquals("Farmer", com.example.model.UserProfile().name)
    }

    @Test
    fun testMockWeatherRiskThresholdEvaluator() {
        val weatherService = com.example.data.api.MockWeatherService()

        // 1. Test High Humidity Threshold (>80% RH) for Late Blight
        val humidCondition = weatherService.createSimulatedReading(
            temp = 22f,
            humidity = 90f,
            leafWetness = 7f,
            rain = 12f
        )
        val humidAlerts = weatherService.evaluateRiskThresholds(humidCondition)
        assertTrue(humidAlerts.any { it.targetDisease.contains("Late Blight") })
        assertTrue(humidAlerts.any { it.severity == ThreatSeverity.CRITICAL || it.severity == ThreatSeverity.HIGH })

        // 2. Test High Temperature Threshold (>33°C) for Sucking Pests
        val heatCondition = weatherService.createSimulatedReading(
            temp = 37f,
            humidity = 40f,
            leafWetness = 0.5f,
            rain = 0f
        )
        val heatAlerts = weatherService.evaluateRiskThresholds(heatCondition)
        assertTrue(heatAlerts.any { it.targetDisease.contains("Whitefly") || it.targetDisease.contains("Thrip") })
        assertTrue(heatAlerts.any { it.severity == ThreatSeverity.CRITICAL })

        // 3. Test Prolonged Leaf Wetness Threshold (>6 hours) for Bacterial Blight
        val wetCondition = weatherService.createSimulatedReading(
            temp = 25f,
            humidity = 75f,
            leafWetness = 9f,
            rain = 20f
        )
        val wetAlerts = weatherService.evaluateRiskThresholds(wetCondition)
        assertTrue(wetAlerts.any { it.targetDisease.contains("Bacterial Leaf Spot") })
    }

    @Test
    fun testIPMDecisionTreeGraphIntegrity() {
        val treeNodes = com.example.data.local.IPMDecisionTreeData.decisionTreeNodes
        val recommendations = com.example.data.local.IPMDecisionTreeData.allRecommendations

        // Root must exist
        assertTrue(treeNodes.containsKey("root"))

        // Ensure all options point to valid next nodes or valid recommendation IDs
        treeNodes.values.forEach { node ->
            assertTrue(node.options.isNotEmpty())
            node.options.forEach { option ->
                if (option.nextNodeId != null) {
                    assertTrue(
                        "Node ${node.nodeId} option ${option.optionId} points to invalid nextNodeId ${option.nextNodeId}",
                        treeNodes.containsKey(option.nextNodeId)
                    )
                }
                if (option.finalRecommendationId != null) {
                    assertTrue(
                        "Node ${node.nodeId} option ${option.optionId} points to invalid recommendation ${option.finalRecommendationId}",
                        recommendations.any { it.diseaseId == option.finalRecommendationId }
                    )
                }
            }
        }

        // Ensure each recommendation contains 4 tiers of IPM protocols
        recommendations.forEach { rec ->
            assertTrue("Rec ${rec.diseaseId} missing cultural actions", rec.culturalActions.isNotEmpty())
            assertTrue("Rec ${rec.diseaseId} missing mechanical actions", rec.mechanicalActions.isNotEmpty())
            assertTrue("Rec ${rec.diseaseId} missing biological actions", rec.biologicalActions.isNotEmpty())
            assertTrue("Rec ${rec.diseaseId} missing chemical actions", rec.chemicalActions.isNotEmpty())
        }

        // Test search filter
        val blightSearchResults = com.example.data.local.IPMDecisionTreeData.searchRecommendations("blight")
        assertTrue(blightSearchResults.size >= 2)

        val chilliSearchResults = com.example.data.local.IPMDecisionTreeData.searchRecommendations("chilli")
        assertTrue(chilliSearchResults.isNotEmpty())

        val armywormSearchResults = com.example.data.local.IPMDecisionTreeData.searchRecommendations("armyworm")
        assertTrue(armywormSearchResults.isNotEmpty())
        assertEquals("rec_fall_armyworm", armywormSearchResults.first().diseaseId)
    }

    @Test
    fun testDiagnosisHistoryFiltering() {
        val sampleReports = listOf(
            com.example.model.CropReport(
                id = 1L,
                farmerName = "Ramesh Kumar",
                villageRegion = "East Field",
                cropName = "Tomato",
                variety = "Abhinav",
                cropStage = "Fruiting",
                soilCondition = "Loamy",
                weatherFactors = "Humid",
                symptomDescription = "Brown blotches on foliage",
                diseaseName = "Late Blight",
                scientificName = "Phytophthora infestans",
                confidence = 94,
                severity = ThreatSeverity.CRITICAL,
                causeSummary = "Fungal infection",
                weatherRiskCorrelation = "High humidity",
                ipmBiological = "Trichoderma",
                ipmCultural = "Pruning",
                ipmChemical = "Mancozeb",
                safeDosage = "2.5 g/L",
                preHarvestIntervalDays = 7,
                safetyPpeInstructions = "Gloves",
                labReferralRecommended = true
            ),
            com.example.model.CropReport(
                id = 2L,
                farmerName = "Anita Singh",
                villageRegion = "North Block",
                cropName = "Chilli",
                variety = "Teja",
                cropStage = "Vegetative",
                soilCondition = "Sandy",
                weatherFactors = "Dry",
                symptomDescription = "Upward curling and yellowing",
                diseaseName = "Leaf Curl Virus",
                scientificName = "Begomovirus",
                confidence = 91,
                severity = ThreatSeverity.HIGH,
                causeSummary = "Whitefly vector",
                weatherRiskCorrelation = "Warm dry spell",
                ipmBiological = "Chrysoperla",
                ipmCultural = "Yellow sticky traps",
                ipmChemical = "Acetamiprid",
                safeDosage = "0.5 g/L",
                preHarvestIntervalDays = 10,
                safetyPpeInstructions = "Mask",
                labReferralRecommended = false
            )
        )

        // Filter by text search "curling"
        val queryResult = sampleReports.filter { it.symptomDescription.contains("curling", ignoreCase = true) }
        assertEquals(1, queryResult.size)
        assertEquals("Chilli", queryResult.first().cropName)

        // Filter by crop "Tomato"
        val cropResult = sampleReports.filter { it.cropName.contains("Tomato", ignoreCase = true) }
        assertEquals(1, cropResult.size)
        assertEquals("Late Blight", cropResult.first().diseaseName)

        // Combined filter
        val combined = sampleReports.filter {
            it.diseaseName.contains("Blight", ignoreCase = true) && it.cropName.contains("Tomato", ignoreCase = true)
        }
        assertEquals(1, combined.size)
    }

    @Test
    fun testGeospatialHotspotAggregation() {
        val sampleReports = listOf(
            com.example.model.CropReport(
                id = 1L,
                farmerName = "Ramesh Kumar",
                villageRegion = "East Field",
                cropName = "Tomato",
                variety = "Abhinav",
                cropStage = "Fruiting",
                soilCondition = "Loamy",
                weatherFactors = "Humid",
                symptomDescription = "Brown blotches on foliage",
                diseaseName = "Late Blight",
                scientificName = "Phytophthora infestans",
                confidence = 94,
                severity = ThreatSeverity.CRITICAL,
                causeSummary = "Fungal infection",
                weatherRiskCorrelation = "High humidity",
                ipmBiological = "Trichoderma",
                ipmCultural = "Pruning",
                ipmChemical = "Mancozeb",
                safeDosage = "2.5 g/L",
                preHarvestIntervalDays = 7,
                safetyPpeInstructions = "Gloves",
                labReferralRecommended = true,
                latitude = 18.525,
                longitude = 73.850
            ),
            com.example.model.CropReport(
                id = 2L,
                farmerName = "Suresh Patel",
                villageRegion = "East Field",
                cropName = "Tomato",
                variety = "Himsona",
                cropStage = "Fruiting",
                soilCondition = "Loamy",
                weatherFactors = "Humid",
                symptomDescription = "Stem lesions",
                diseaseName = "Late Blight",
                scientificName = "Phytophthora infestans",
                confidence = 92,
                severity = ThreatSeverity.CRITICAL,
                causeSummary = "Fungal infection",
                weatherRiskCorrelation = "High humidity",
                ipmBiological = "Trichoderma",
                ipmCultural = "Pruning",
                ipmChemical = "Mancozeb",
                safeDosage = "2.5 g/L",
                preHarvestIntervalDays = 7,
                safetyPpeInstructions = "Gloves",
                labReferralRecommended = true,
                latitude = 18.526,
                longitude = 73.851
            ),
            com.example.model.CropReport(
                id = 3L,
                farmerName = "Anita Singh",
                villageRegion = "North Block",
                cropName = "Chilli",
                variety = "Teja",
                cropStage = "Vegetative",
                soilCondition = "Sandy",
                weatherFactors = "Dry",
                symptomDescription = "Upward curling",
                diseaseName = "Leaf Curl Virus",
                scientificName = "Begomovirus",
                confidence = 91,
                severity = ThreatSeverity.HIGH,
                causeSummary = "Whitefly vector",
                weatherRiskCorrelation = "Warm dry spell",
                ipmBiological = "Chrysoperla",
                ipmCultural = "Yellow sticky traps",
                ipmChemical = "Acetamiprid",
                safeDosage = "0.5 g/L",
                preHarvestIntervalDays = 10,
                safetyPpeInstructions = "Mask",
                labReferralRecommended = false,
                latitude = 18.538,
                longitude = 73.864
            )
        )

        val clusters = com.example.ui.components.aggregateDiagnosisData(sampleReports, emptyList())
        assertEquals(2, clusters.size)

        val eastFieldCluster = clusters.find { it.sectorName == "East Field" }
        assertNotNull(eastFieldCluster)
        assertEquals(2, eastFieldCluster?.reportCount)
        assertEquals(ThreatSeverity.CRITICAL, eastFieldCluster?.maxSeverity)
        assertEquals("Late Blight", eastFieldCluster?.primaryDisease)
        assertEquals("Tomato", eastFieldCluster?.primaryCrop)

        val northBlockCluster = clusters.find { it.sectorName == "North Block" }
        assertNotNull(northBlockCluster)
        assertEquals(1, northBlockCluster?.reportCount)
        assertEquals(ThreatSeverity.HIGH, northBlockCluster?.maxSeverity)
        assertEquals("Leaf Curl Virus", northBlockCluster?.primaryDisease)
    }

    @Test
    fun testDynamicDayLabelsGeneration() {
        val days = com.example.util.MicroclimateRiskEngine.generateDynamicDayLabels(6)
        assertEquals(6, days.size)
        assertEquals("Today", days[0].first)
        assertEquals("Tomorrow", days[1].first)
        assertEquals("Day 3", days[2].first)
        assertTrue(days[0].second.isNotEmpty())
        assertTrue(days[1].second.isNotEmpty())
    }

    @Test
    fun testMicroclimateRiskEngineFungalRisk() {
        val engine = com.example.util.MicroclimateRiskEngine

        // High Humidity (>82%) + Rain in Fungal Temperature window -> CRITICAL
        val criticalRisk = engine.computeFungalSporeRisk(
            humidityMax = 92,
            humidityMin = 84,
            tempMax = 24,
            tempMin = 18,
            rainProb = 80,
            rainMm = 15f
        )
        assertEquals(ThreatSeverity.CRITICAL, criticalRisk)

        // Moderate Humidity -> MODERATE
        val modRisk = engine.computeFungalSporeRisk(
            humidityMax = 68,
            humidityMin = 50,
            tempMax = 28,
            tempMin = 20,
            rainProb = 20,
            rainMm = 0f
        )
        assertEquals(ThreatSeverity.MODERATE, modRisk)

        // Dry microclimate -> LOW
        val lowRisk = engine.computeFungalSporeRisk(
            humidityMax = 45,
            humidityMin = 30,
            tempMax = 32,
            tempMin = 22,
            rainProb = 5,
            rainMm = 0f
        )
        assertEquals(ThreatSeverity.LOW, lowRisk)
    }

    @Test
    fun testMicroclimateRiskEnginePestRisk() {
        val engine = com.example.util.MicroclimateRiskEngine

        // Heatwave condition (>34°C) -> CRITICAL
        val criticalPest = engine.computePestVectorRisk(
            tempMax = 36,
            tempMin = 26,
            humidityMin = 38,
            windSpeedKmh = 14f
        )
        assertEquals(ThreatSeverity.CRITICAL, criticalPest)

        // Warm with active wind -> HIGH
        val highPest = engine.computePestVectorRisk(
            tempMax = 32,
            tempMin = 22,
            humidityMin = 55,
            windSpeedKmh = 16f
        )
        assertEquals(ThreatSeverity.HIGH, highPest)

        // Cool weather -> LOW
        val lowPest = engine.computePestVectorRisk(
            tempMax = 19,
            tempMin = 12,
            humidityMin = 65,
            windSpeedKmh = 6f
        )
        assertEquals(ThreatSeverity.LOW, lowPest)
    }

    @Test
    fun testAppBrandingAndSplashLocalization() {
        assertEquals("AgriShield", com.example.util.AppStrings.appName())
        assertEquals("Intelligent Crop Health & Outbreak Defense", com.example.util.AppStrings.appTagline(AppLanguage.ENGLISH))
        assertTrue(com.example.util.AppStrings.appTagline(AppLanguage.HINDI).isNotEmpty())
        assertTrue(com.example.util.AppStrings.appTagline(AppLanguage.MARATHI).isNotEmpty())
        assertTrue(com.example.util.AppStrings.appTagline(AppLanguage.SPANISH).isNotEmpty())
        assertTrue(com.example.util.AppStrings.appTagline(AppLanguage.SWAHILI).isNotEmpty())
        assertTrue(com.example.util.AppStrings.appTagline(AppLanguage.BENGALI).isNotEmpty())
        assertTrue(com.example.util.AppStrings.appTagline(AppLanguage.FRENCH).isNotEmpty())
    }
}


