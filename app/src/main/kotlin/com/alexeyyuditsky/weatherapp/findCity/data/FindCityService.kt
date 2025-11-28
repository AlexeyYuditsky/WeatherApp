package com.alexeyyuditsky.weatherapp.findCity.data

import com.alexeyyuditsky.weatherapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface FindCityService {

    @GET("geo/1.0/direct")
    suspend fun findCity(
        @Query("q") query: String,
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
    ): List<FoundCityCloud>
}