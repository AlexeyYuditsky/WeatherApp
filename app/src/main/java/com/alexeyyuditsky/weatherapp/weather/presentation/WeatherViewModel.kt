package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository,
    private val runAsync: RunAsync,
    private val mapper: WeatherResult.Mapper<WeatherUi>,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow(KEY, mapper.mapEmpty())

    init {
        loadWeather()
    }

    fun loadWeather() = runAsync.invoke(
        scope = viewModelScope,
        background = {
            val weatherResult = repository.fetchWeather()
            val weatherUi = weatherResult.map(mapper = mapper)
            weatherUi
        },
        ui = { weatherScreenUiBase ->
            savedStateHandle[KEY] = weatherScreenUiBase
        }
    )

    private companion object {
        const val KEY = "WeatherScreenUiKey"
    }
}