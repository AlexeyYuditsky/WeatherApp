package com.alexeyyuditsky.weatherapp.weather.domain

import javax.inject.Inject
import javax.inject.Singleton

interface RefreshWeatherUseCase {

    suspend operator fun invoke()

    @Singleton
    class Base @Inject constructor(
        private val repository: WeatherRepository,
    ) : RefreshWeatherUseCase {

        override suspend operator fun invoke() =
            repository.refreshWeather()
    }
}