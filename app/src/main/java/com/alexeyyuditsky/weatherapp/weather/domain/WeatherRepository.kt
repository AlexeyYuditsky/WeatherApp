package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun weather(
        savedWeather: WeatherParams,
    ): WeatherResult

    fun weatherFlow(): Flow<WeatherParams>

    fun errorFlow(): Flow<Boolean>

    fun loadWeather()
}