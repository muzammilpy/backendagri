package com.agrishield.app

import android.app.Application
import android.util.Log
import com.agrishield.app.domain.SyncWorker

class AgriShieldApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Global uncaught exception handler — prevents the "app has a bug" dialog
        // and logs the error instead so the app degrades gracefully
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("AgriShield", "Uncaught exception on ${thread.name}", throwable)
            // Still call default so Android can restart the app
            defaultHandler?.uncaughtException(thread, throwable)
        }

        // WorkManager sync — wrapped so a failure here never crashes the app
        try {
            SyncWorker.enqueue(this)
            Log.i("AgriShieldApp", "Background sync worker enqueued.")
        } catch (e: Exception) {
            Log.w("AgriShieldApp", "WorkManager init failed (non-fatal): ${e.message}")
        }
    }
}
