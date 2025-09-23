package com.alexeyyuditsky.weatherapp.weather.data

import javax.inject.Inject

interface WeatherCloudDataSource {

    suspend fun temperature(
        latitude: Float,
        longitude: Float,
    ): Float

    class Base @Inject constructor(
        private val weatherService: WeatherService,
    ) : WeatherCloudDataSource {

        override suspend fun temperature(
            latitude: Float,
            longitude: Float,
        ): Float {
           return weatherService.fetchWeather(
                latitude = latitude,
                longitude = longitude,
            ).main.temperature
        }

    }

}