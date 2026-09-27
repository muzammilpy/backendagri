package com.agrishield.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for a pest pheromone trap log entry.
 * [isExceeded] is true when [mothCount] >= [etlThreshold].
 */
@Entity(tableName = "trap_logs")
data class TrapLog(
    @PrimaryKey val id: String,
    val district: String,
    val crop: String,
    val trapType: String,
    val mothCount: Int,
    val etlThreshold: Int,
    val isExceeded: Boolean,
    val loggedAt: Long,
    val syncStatus: String = "PENDING"
)
