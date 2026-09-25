package com.example.data.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.data.repository.CropHealthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

/**
 * Background WorkManager worker that fetches live weather forecast data
 * from Open-Meteo on a daily schedule (or early morning/midnight) to ensure
 * farmers always have up-to-date disease & pest risk warnings.
 */
class WeatherSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "WeatherSyncWorker starting daily microclimate sync...")
        return try {
            val repository = CropHealthRepository.getInstance(
                applicationContext,
                CoroutineScope(Dispatchers.IO)
            )
            repository.syncLiveWeatherFromOpenMeteo()
            Log.d(TAG, "WeatherSyncWorker daily sync succeeded.")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "WeatherSyncWorker failed: ${e.message}", e)
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.success()
            }
        }
    }

    companion object {
        private const val TAG = "WeatherSyncWorker"
        const val WORK_NAME_PERIODIC = "daily_weather_microclimate_sync"
        const val WORK_NAME_ONETIME = "onetime_weather_microclimate_sync"
    }
}

/**
 * Manager to schedule and trigger periodic daily & on-demand weather background synchronization.
 */
object WeatherSyncManager {

    /**
     * Enqueues a periodic WorkManager request to refresh microclimate weather daily.
     */
    fun scheduleDailyWeatherSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<WeatherSyncWorker>(
            24, TimeUnit.HOURS,
            6, TimeUnit.HOURS // flex interval
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15, TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WeatherSyncWorker.WORK_NAME_PERIODIC,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }

    /**
     * Triggers an immediate one-time background sync request.
     */
    fun triggerImmediateSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<WeatherSyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WeatherSyncWorker.WORK_NAME_ONETIME,
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }
}
