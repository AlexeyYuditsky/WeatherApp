package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import java.io.IOException
import javax.inject.Inject

interface WeatherCloudDataSource {

    suspend fun temperature(
        latitude: Float,
        longitude: Float,
    ): WeatherCloud

    class Base @Inject constructor(
        private val weatherService: WeatherService,
    ) : WeatherCloudDataSource {

        override suspend fun temperature(
            latitude: Float,
            longitude: Float,
        ): WeatherCloud = try {
            weatherService.fetchWeather(
                latitude = latitude,
                longitude = longitude,
            )
        } catch (e: Exception) {
            if (e is IOException)
                throw NoInternetException
            else
                throw e
        }
    }
}