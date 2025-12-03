package com.alexeyyuditsky.weatherapp.weather.presentation

import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ErrorWeatherUiMapper {

    val state: Flow<ErrorUi>

    class Base @Inject constructor(
        repository: WeatherRepository,
    ) : ErrorWeatherUiMapper {

        override val state: Flow<ErrorUi> = repository.hasErrorFlow
            .map { hasError ->
                if (hasError)
                    ErrorUi.Error
                else
                    ErrorUi.Empty
            }
    }
}