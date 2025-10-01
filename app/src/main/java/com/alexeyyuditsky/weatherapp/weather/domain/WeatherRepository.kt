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
            val (latitude, longitude, cityName) = weatherCacheDataSource.cityParams()
            val temperature = weatherCloudDataSource.temperature(
                latitude = latitude,
                longitude = longitude
            )
            WeatherResult.Base(
                weatherInCity = WeatherInCity(
                    cityName = cityName,
                    temperature = temperature
                )
            )
        } catch (e: DomainException) {
            WeatherResult.Failed(error = e)
        }
    }

}