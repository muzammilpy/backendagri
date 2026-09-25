package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Data models for the Integrated Pest Management (IPM) Decision Tree Advisor
 */

enum class IPMInterventionTier(
    val title: String,
    val description: String,
    val priority: Int
) {
    CULTURAL(
        title = "1. Cultural & Agronomic Practices",
        description = "Sanitation, spacing, canopy aeration, resistant crop varieties, and irrigation management",
        priority = 1
    ),
    MECHANICAL(
        title = "2. Mechanical & Physical Traps",
        description = "Pheromone traps, sticky sheets, manual rogueing, bird perches, and netting",
        priority = 2
    ),
    BIOLOGICAL(
        title = "3. Biological & Botanical Bio-controls",
        description = "Beneficial predatory insects, entomopathogens (Trichoderma, Beauveria, Bt), and botanical neem extracts",
        priority = 3
    ),
    CHEMICAL_RATIONAL(
        title = "4. Targeted Chemical & Bio-rationals (Last Resort)",
        description = "Narrow-spectrum, minimal-residue targeted intervention with strict pre-harvest intervals (PHI)",
        priority = 4
    )
}

data class IPMActionItem(
    val title: String,
    val instruction: String,
    val dosageOrMethod: String? = null,
    val timingOrFrequency: String = "As needed during early infestation",
    val safetyWarning: String? = null
)

data class IPMProtocolRecommendation(
    val diseaseId: String,
    val diseaseName: String,
    val scientificName: String,
    val targetCrops: List<String>,
    val pathogenType: String, // "Fungal", "Bacterial", "Viral Vector / Insect", "Oomycete", "Nematode"
    val severityLevel: ThreatSeverity,
    val keySymptomsSummary: String,
    val culturalActions: List<IPMActionItem>,
    val mechanicalActions: List<IPMActionItem>,
    val biologicalActions: List<IPMActionItem>,
    val chemicalActions: List<IPMActionItem>,
    val preHarvestIntervalDays: Int? = null,
    val preventionChecklist: List<String> = emptyList()
)

data class DecisionTreeNode(
    val nodeId: String,
    val question: String,
    val explanation: String,
    val iconName: String = "HelpOutline",
    val options: List<DecisionTreeOption>,
    val cropFilter: String? = null
)

data class DecisionTreeOption(
    val optionId: String,
    val label: String,
    val subtext: String,
    val nextNodeId: String? = null,
    val finalRecommendationId: String? = null
)

data class DecisionTreeHistoryStep(
    val nodeId: String,
    val question: String,
    val selectedOption: DecisionTreeOption
)
