package com.alexeyyuditsky.weatherapp.weather.domain

import javax.inject.Inject

interface LoadWeatherUseCase {

    operator fun invoke()

    class Base @Inject constructor(
        private val repository: WeatherRepository,
    ) : LoadWeatherUseCase {

        override operator fun invoke() = repository.loadWeather()
    }
}