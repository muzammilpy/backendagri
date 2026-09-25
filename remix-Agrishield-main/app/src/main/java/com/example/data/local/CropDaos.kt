package com.example.data.local

import androidx.room.*
import com.example.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CropReportDao {
    @Query("SELECT * FROM crop_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<CropReport>>

    @Query("SELECT * FROM crop_reports WHERE id = :id")
    suspend fun getReportById(id: Long): CropReport?

    @Query("SELECT * FROM crop_reports WHERE status = :status ORDER BY timestamp DESC")
    fun getReportsByStatus(status: ReportStatus): Flow<List<CropReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: CropReport): Long

    @Update
    suspend fun updateReport(report: CropReport)

    @Delete
    suspend fun deleteReport(report: CropReport)

    @Query("UPDATE crop_reports SET status = :status, expertNotes = :notes, expertName = :expertName, validationTimestamp = :time WHERE id = :id")
    suspend fun updateExpertTriage(id: Long, status: ReportStatus, notes: String, expertName: String, time: Long)

    @Query("UPDATE crop_reports SET followUpDay3Checked = :day3, followUpDay7Checked = :day7, followUpDay14Checked = :day14, followUpNotes = :notes WHERE id = :id")
    suspend fun updateFollowUp(id: Long, day3: Boolean, day7: Boolean, day14: Boolean, notes: String)

    @Query("UPDATE crop_reports SET fieldOutcome = :outcome WHERE id = :id")
    suspend fun updateFieldOutcome(id: Long, outcome: String)
}

@Dao
interface PestTrapDao {
    @Query("SELECT * FROM pest_traps ORDER BY timestamp DESC")
    fun getAllTrapLogs(): Flow<List<PestTrapLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrapLog(log: PestTrapLog): Long

    @Delete
    suspend fun deleteTrapLog(log: PestTrapLog)
}

@Dao
interface SensorDao {
    @Query("SELECT * FROM sensor_readings ORDER BY timestamp DESC LIMIT 30")
    fun getRecentReadings(): Flow<List<SensorReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: SensorReading): Long
}

@Dao
interface HotspotDao {
    @Query("SELECT * FROM hotspot_zones ORDER BY confirmedReportsCount DESC")
    fun getAllHotspots(): Flow<List<HotspotZone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotspot(hotspot: HotspotZone): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(hotspots: List<HotspotZone>)

    @Query("SELECT * FROM official_alerts ORDER BY id DESC")
    fun getAllAlerts(): Flow<List<OfficialAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: OfficialAlert): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlerts(alerts: List<OfficialAlert>)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("UPDATE user_profiles SET name = :name WHERE id = 1")
    suspend fun updateName(name: String)

    @Query("UPDATE user_profiles SET selectedLanguageCode = :code WHERE id = 1")
    suspend fun updateLanguage(code: String)

    @Query("SELECT * FROM user_profiles WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Query("UPDATE user_profiles SET staffUnlocked = :unlocked, role = :roleLabel WHERE id = 1")
    suspend fun updateStaffUnlock(unlocked: Boolean, roleLabel: String)
}

