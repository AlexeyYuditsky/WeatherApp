package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather")
    fun weather(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
    ): WeatherCloud

    @GET("data/2.5/air_pollution")
    fun airPollution(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
    ): AirPollutionCloud

    @GET("data/2.5/forecast")
    fun forecast(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
    ): ForecastCloud
}