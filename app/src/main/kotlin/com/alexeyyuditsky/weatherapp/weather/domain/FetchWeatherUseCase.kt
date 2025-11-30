package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import javax.inject.Inject

interface FetchWeatherUseCase {

    fun invoke(savedWeather: WeatherParams): WeatherResult

    class Base @Inject constructor(
        private val repository: WeatherRepository,
    ) : FetchWeatherUseCase {

        override fun invoke(savedWeather: WeatherParams): WeatherResult =
            repository.fetchWeather(savedWeather)
    }
}