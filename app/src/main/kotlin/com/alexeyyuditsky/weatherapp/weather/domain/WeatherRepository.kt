package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    val cachedWeatherFlow: Flow<WeatherParams>

    val hasErrorFlow: Flow<Boolean>

    fun fetchWeather(savedWeather: WeatherParams): WeatherResult

    fun loadWeather()

    suspend fun refreshWeather()

    suspend fun saveException(exception: DomainException)
}