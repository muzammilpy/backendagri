package com.agrishield.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a completed on-device crop disease diagnosis.
 *
 * The [confidenceScore] is the raw softmax output from the TFLite classifier
 * in the range [0.0, 1.0] — never hardcoded or estimated.
 * The [syncStatus] drives the offline→backend sync lifecycle:
 *   PENDING = saved locally, not yet uploaded
 *   SYNCED  = successfully uploaded to the backend
 */
@Entity(tableName = "diagnosis_records")
data class DiagnosisRecord(
    @PrimaryKey val id: String,
    val diseaseLabel: String,
    val confidenceScore: Float,
    val severity: String,
    val cropName: String,
    val imagePath: String?,
    val ipmImmediate: String,
    val ipmBiological: String,
    val ipmCultural: String,
    val ipmChemical: String,
    val district: String,
    val timestamp: Long,
    val syncStatus: String = "PENDING",
    val remoteReportId: String? = null
)
