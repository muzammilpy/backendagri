package com.agrishield.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for the local user profile.
 * JWT token is NOT stored here — it is stored exclusively in
 * EncryptedSharedPreferences via SecurePrefs to prevent token
 * leakage through database access.
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val userId: String,
    val name: String,
    val role: String,
    val district: String,
    val staffUnlocked: Boolean = false
)
