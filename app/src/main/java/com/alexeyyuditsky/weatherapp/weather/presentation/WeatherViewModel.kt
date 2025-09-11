package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.StateFlow

class WeatherViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
    runAsync: RunAsync,
) : ViewModel() {

    val state: StateFlow<WeatherScreenUi> =
        savedStateHandle.getStateFlow(KEY, WeatherScreenUi.Empty)

    init {
        runAsync.invoke(
            scope = viewModelScope,
            background = {
                val weatherInCity = weatherRepository.fetchWeather()
                WeatherScreenUi.Base(
                    cityName = weatherInCity.cityName,
                    temperature = weatherInCity.temperature.toString() + "°C"
                )
            },
            ui = { weatherScreenUiBase ->
                savedStateHandle[KEY] = weatherScreenUiBase
            }
        )
    }

    private companion object {
        const val KEY = "WeatherScreenUiKey"
    }

}