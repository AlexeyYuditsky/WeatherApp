package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCloudDataSource
import javax.inject.Inject

interface WeatherRepository {

    suspend fun fetchWeather(): WeatherResult

    class Base @Inject constructor(
        private val weatherCacheDataSource: WeatherCacheDataSource,
        private val weatherCloudDataSource: WeatherCloudDataSource,
    ) : WeatherRepository {

        override suspend fun fetchWeather(): WeatherResult = try {
            val (latitude, longitude) = weatherCacheDataSource.cityParams()
            val weatherCloud = weatherCloudDataSource.temperature(
                latitude = latitude,
                longitude = longitude
            )
            WeatherResult.Success(
                weatherInCity = WeatherInCity(
                    cityName = weatherCloud.cityName,
                    temperature = weatherCloud.main.temperature
                )
            )
        } catch (e: DomainException) {
            WeatherResult.Error(error = e)
        }
    }
}