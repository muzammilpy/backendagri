package com.agrishield.app.data.remote

import com.agrishield.app.data.model.OpenMeteoResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude")                            lat: Double,
        @Query("longitude")                           lon: Double,
        @Query("daily") daily: String =
            "temperature_2m_max,temperature_2m_min," +
            "relative_humidity_2m_max,relative_humidity_2m_min," +
            "precipitation_sum,precipitation_probability_max," +
            "wind_speed_10m_max,weather_code",
        @Query("timezone")                            timezone: String = "auto",
        @Query("forecast_days")                       days: Int = 7
    ): OpenMeteoResponse
}

object WeatherApiClient {
    val service: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}
