package com.agrishield.app.domain

import android.content.Context
import android.util.Log
import androidx.work.*
import com.agrishield.app.data.local.AppDatabase
import com.agrishield.app.data.model.SyncPayload
import com.agrishield.app.data.remote.SyncApiClient
import java.util.concurrent.TimeUnit

/**
 * SyncWorker
 * ==========
 * Runs in background via WorkManager.
 * Picks up all diagnoses that haven't been synced to the server yet
 * and uploads them when a network connection is available.
 *
 * This is what makes the app offline-first:
 *  - Farmer takes photo in Yavatmal field with no signal -> saved to Room DB
 *  - When phone reaches town / wifi -> WorkManager runs this worker
 *  - Report appears on government surveillance dashboard
 */
class SyncWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    companion object {
        private const val TAG = "SyncWorker"
        const val WORK_NAME = "agrishield_sync"

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<SyncWorker>(
                repeatInterval = 30, repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
            Log.i(TAG, "SyncWorker enqueued (every 30 min when connected)")
        }

        /** One-shot sync — call this when user manually requests sync */
        fun runNow(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    override suspend fun doWork(): Result {
        val dao = AppDatabase.getInstance(applicationContext).diagnosisDao()
        val unsynced = dao.getUnsynced()

        if (unsynced.isEmpty()) {
            Log.i(TAG, "Nothing to sync.")
            return Result.success()
        }

        Log.i(TAG, "Syncing ${unsynced.size} unsynced diagnoses...")
        var failCount = 0

        for (entity in unsynced) {
            try {
                val payload = SyncPayload(
                    farmerId   = entity.id,
                    farmLat    = entity.farmLat,
                    farmLon    = entity.farmLon,
                    district   = entity.districtName,
                    crop       = entity.cropLabel,
                    diagnosis  = entity.diagnosisLabel,
                    confidence = entity.confidence,
                    severity   = entity.severity,
                    symptoms   = "",
                    imageBase64 = null        // skip image in background sync to save bandwidth
                )
                val response = SyncApiClient.service.uploadDiagnosis(payload)
                if (response.success && response.reportId != null) {
                    dao.markSynced(entity.id, response.reportId)
                    Log.i(TAG, "Synced ${entity.id} -> server report ${response.reportId}")
                } else {
                    Log.w(TAG, "Server rejected ${entity.id}: ${response.error}")
                    failCount++
                }
            } catch (e: Exception) {
                Log.e(TAG, "Sync failed for ${entity.id}: ${e.message}")
                failCount++
            }
        }

        return if (failCount == 0) Result.success()
        else Result.retry()    // retry with exponential backoff
    }
}
