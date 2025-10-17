package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import com.alexeyyuditsky.weatherapp.findCity.domain.ServiceUnavailableException
import java.io.IOException
import javax.inject.Inject

interface WeatherCloudDataSource {

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

    abstract class Abstract : WeatherCloudDataSource {

        protected suspend fun <T> handle(
            block: suspend () -> T,
        ): T = try {
            block.invoke()
        } catch (e: Exception) {
            if (e is IOException)
                throw NoInternetException
            else
                throw ServiceUnavailableException
        }
    }

    class Base @Inject constructor(
        private val service: WeatherService,
    ) : Abstract() {

        override suspend fun weather(
            latitude: Float,
            longitude: Float
        ) = handle { service.weather(latitude, longitude) }

        override suspend fun airPollution(
            latitude: Float,
            longitude: Float
        ) = handle { service.airPollution(latitude, longitude) }

        override suspend fun forecast(
            latitude: Float,
            longitude: Float
        ) = handle { service.forecast(latitude, longitude) }
    }
}