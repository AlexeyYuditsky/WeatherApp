package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.ConnectionUiMapper
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi
import com.alexeyyuditsky.weatherapp.weather.domain.CachedWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.FetchWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.LoadWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val runAsync: RunAsync,
    private val loadWeatherUseCase: LoadWeatherUseCase,
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    cachedWeatherUseCase: CachedWeatherUseCase,
    weatherResultMapper: WeatherResult.Mapper<WeatherUi>,
    errorWeatherUiMapper: ErrorWeatherUiMapper,
    connectionUiMapper: ConnectionUiMapper,
) : ViewModel() {

    val state: StateFlow<WeatherUi> = savedStateHandle.getStateFlow(KEY, WeatherUi.Empty)

    val connection: StateFlow<ConnectionUi> = connectionUiMapper.state

    val error: StateFlow<ErrorUi> = errorWeatherUiMapper.state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
        initialValue = ErrorUi.Empty
    )

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = cachedWeatherUseCase.weather,
            background = { weatherParams ->
                val weatherResult = fetchWeatherUseCase.invoke(savedWeather = weatherParams)
                val weatherUi = weatherResult.map(weatherResultMapper)
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
                loadWeatherUseCase.invoke()
            }
        )
    }

    private companion object {
        const val KEY = "WeatherScreenUiKey"
    }
}