package com.agrishield.app.data.local

import android.content.Context
import androidx.room.*
import com.agrishield.app.data.model.DiagnosisEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisEntity)

    @Query("SELECT * FROM diagnoses ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<DiagnosisEntity>>

    @Query("SELECT * FROM diagnoses ORDER BY createdAt DESC")
    suspend fun getAll(): List<DiagnosisEntity>

    @Query("SELECT * FROM diagnoses WHERE id = :id")
    suspend fun getById(id: String): DiagnosisEntity?

    @Query("SELECT * FROM diagnoses WHERE isSyncedToServer = 0 ORDER BY createdAt ASC")
    suspend fun getUnsynced(): List<DiagnosisEntity>

    @Query("UPDATE diagnoses SET isSyncedToServer = 1, serverReportId = :reportId WHERE id = :id")
    suspend fun markSynced(id: String, reportId: String)

    @Query("UPDATE diagnoses SET feedbackOutcome = :outcome, feedbackNotes = :notes WHERE id = :id")
    suspend fun saveFeedback(id: String, outcome: String, notes: String)

    @Query("SELECT COUNT(*) FROM diagnoses")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM diagnoses WHERE isSyncedToServer = 0")
    suspend fun unsyncedCount(): Int

    @Query("SELECT severity, COUNT(*) as cnt FROM diagnoses GROUP BY severity")
    suspend fun severityBreakdown(): List<SeverityCount>

    @Query("DELETE FROM diagnoses WHERE id = :id")
    suspend fun delete(id: String)
}

data class SeverityCount(val severity: String, val cnt: Int)

@Database(
    entities = [DiagnosisEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agrishield.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
