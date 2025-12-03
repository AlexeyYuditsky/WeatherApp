package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CachedWeatherUseCase {

    val cachedWeatherFlow: Flow<WeatherParams>

    class Base @Inject constructor(
        repository: WeatherRepository,
    ) : CachedWeatherUseCase {

        override val cachedWeatherFlow: Flow<WeatherParams> = repository.cachedWeatherFlow
    }
}