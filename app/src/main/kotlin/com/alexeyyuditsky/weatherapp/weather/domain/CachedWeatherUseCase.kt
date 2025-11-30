package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CachedWeatherUseCase {

    val weather: Flow<WeatherParams>

    class Base @Inject constructor(
        repository: WeatherRepository,
    ) : CachedWeatherUseCase {

        override val weather: Flow<WeatherParams> = repository.weatherFlow
    }
}