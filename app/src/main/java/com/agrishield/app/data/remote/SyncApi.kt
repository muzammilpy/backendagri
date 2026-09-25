package com.agrishield.app.data.remote

import com.agrishield.app.data.model.SyncPayload
import com.agrishield.app.data.model.SyncResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface SyncApiService {
    @POST("api/diagnose")
    suspend fun uploadDiagnosis(@Body payload: SyncPayload): SyncResponse
}

object SyncApiClient {
    // Points to the agrishield.py Python server.
    // For production, replace with your deployed server URL.
    private const val BASE_URL = "http://10.0.2.2:8000/"   // localhost from emulator

    val service: SyncApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SyncApiService::class.java)
    }
}
