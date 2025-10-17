package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.Connection
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WeatherRepository,
    private val runAsync: RunAsync,
    private val mapper: WeatherResult.Mapper<WeatherUi>,
    connection: Connection,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow(KEY, mapper.mapToEmpty())

    val connectionFlow = connection.connected.map {
        if (it) ConnectedUi.Connected else ConnectedUi.Disconnected
    }

    val errorFlow = repository.errorFlow().map {
        if (it) ErrorUi.Error else ErrorUi.Empty
    }

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = repository.weatherFlow(),
            map = { weather -> repository.weather(savedWeather = weather).map(mapper) },
            onEach = { savedStateHandle[KEY] = it }
        )
    }

    fun retryLoadWeather() {
        savedStateHandle[KEY] = WeatherUi.Loading
        runAsync.runAsync(
            scope = viewModelScope,
            background = { repository.loadWeather() }
        )
    }

    private companion object {
        const val KEY = "WeatherScreenUiKey"
    }
}