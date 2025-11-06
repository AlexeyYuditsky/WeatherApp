package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.ConnectionUiMapper
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository,
    private val runAsync: RunAsync,
    private val mapper: WeatherResult.Mapper<WeatherUi>,
    connectionUiMapper: ConnectionUiMapper,
) : ViewModel() {

    val state: StateFlow<WeatherUi> = savedStateHandle.getStateFlow(KEY, WeatherUi.Empty)
    val connection: SharedFlow<ConnectionUi> = connectionUiMapper.state

    val error = repository.errorFlow.map {
        if (it)
            ErrorUi.Error
        else
            ErrorUi.Empty
    }

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = repository.weatherFlow,
            background = { weatherParams ->
                val weatherResult = repository.weather(savedWeather = weatherParams)
                val weatherUi = weatherResult.map(mapper)
                weatherUi
            },
            ui = { weatherUi ->
                savedStateHandle[KEY] = weatherUi
            }
        )
    }

    fun retryLoadWeather() {
        savedStateHandle[KEY] = WeatherUi.Loading
        runAsync.runAsync(
            scope = viewModelScope,
            background = {
                repository.loadWeather()
            }
        )
    }

    private companion object {
        const val KEY = "WeatherScreenUiKey"
    }
}