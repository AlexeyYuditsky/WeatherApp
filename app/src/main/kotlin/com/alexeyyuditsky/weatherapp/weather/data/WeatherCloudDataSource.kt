package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.core.data.CloudDataSource
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherCloudDataSource : CloudDataSource {

    suspend fun weather(
        latitude: Float,
        longitude: Float,
    ): WeatherCloud

    suspend fun airPollution(
        latitude: Float,
        longitude: Float,
    ): AirPollutionCloud

    suspend fun forecast(
        latitude: Float,
        longitude: Float,
    ): ForecastCloud

    @Singleton
    class Base @Inject constructor(
        private val service: WeatherService,
    ) : WeatherCloudDataSource {

        override suspend fun weather(
            latitude: Float,
            longitude: Float,
        ) = handle { service.weather(latitude, longitude) }

        override suspend fun airPollution(
            latitude: Float,
            longitude: Float,
        ) = handle { service.airPollution(latitude, longitude) }

        override suspend fun forecast(
            latitude: Float,
            longitude: Float,
        ) = handle { service.forecast(latitude, longitude) }
    }
}