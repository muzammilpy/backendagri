package com.agrishield.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity storing IPM advisory data bundled from model_metadata.json.
 * All text fields are guaranteed non-empty by the training pipeline.
 */
@Entity(tableName = "ipm_protocols")
data class IpmProtocol(
    @PrimaryKey val diseaseId: String,
    val displayName: String,
    val crop: String,
    val severity: String,
    val immediateAction: String,
    val biological: String,
    val cultural: String,
    val chemical: String,
    val marathi: String,
    val hindi: String
)
