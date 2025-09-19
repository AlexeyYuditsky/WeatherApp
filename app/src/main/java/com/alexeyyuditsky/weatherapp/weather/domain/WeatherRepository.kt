package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCloudDataSource
import javax.inject.Inject

interface WeatherRepository {

    suspend fun fetchWeather(): WeatherInCity

    class Base @Inject constructor(
        private val weatherCacheDataSource: WeatherCacheDataSource,
        private val weatherCloudDataSource: WeatherCloudDataSource,
    ) : WeatherRepository {

        override suspend fun fetchWeather(): WeatherInCity {
            val (latitude, longitude, cityName) = weatherCacheDataSource.cityParams()
            val temperature = weatherCloudDataSource.temperature(
                latitude = latitude,
                longitude = longitude
            )
            return WeatherInCity(
                cityName = cityName,
                temperature = temperature
            )
        }
    }

}