package com.example.util

/**
 * Honest defaults for SIH PS 26131 (GoM / MSInS).
 * Demo staff PIN is the problem-statement ID so judges can unlock extension tools.
 */
object AgriShieldConfig {
    const val PROBLEM_STATEMENT_ID = "26131"
    const val STAFF_PIN = "26131"

    /** Yavatmal — Vidarbha cotton belt, not Pune. */
    const val DEFAULT_LATITUDE = 20.3888
    const val DEFAULT_LONGITUDE = 78.1204
    const val DEFAULT_LOCATION_LABEL = "Yavatmal, Vidarbha, Maharashtra"

    const val CHEMICAL_DISCLAIMER =
        "This is decision support, not a spray order. Confirm in the field. Follow CIBRC-labelled products, Pre-Harvest Interval, and local KVK / agriculture officer advice before applying any pesticide."

    const val MAP_DISCLAIMER =
        "Map pins are this device's diagnoses, trap logs, and labelled district reference cases. They are not live Sentinel / Bhuvan satellite NDVI."

    const val RULE_ENGINE_SOURCE =
        "Offline symptom rules (keywords + crop). The photo is stored but was not classified by a vision model."

    const val GEMINI_SOURCE =
        "Gemini vision suggestion. Treat as a shortlist — scout the plant before spraying."
}
