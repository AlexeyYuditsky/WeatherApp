package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    val weatherFlow: Flow<WeatherParams>

    val errorFlow: Flow<Boolean>

    fun weather(savedWeather: WeatherParams): WeatherResult

    fun loadWeather()
}