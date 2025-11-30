package com.alexeyyuditsky.weatherapp.weather.domain

import javax.inject.Inject

interface LoadWeatherUseCase {

    fun invoke()

    class Base @Inject constructor(
        private val repository: WeatherRepository,
    ) : LoadWeatherUseCase {

        override fun invoke() = repository.loadWeather()
    }
}