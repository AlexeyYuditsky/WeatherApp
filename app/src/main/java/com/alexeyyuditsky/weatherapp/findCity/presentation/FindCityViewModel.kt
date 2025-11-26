package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.ConnectionUiMapper
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class FindCityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FindCityRepository,
    private val mapper: FoundCityResult.Mapper<FoundCityUi>,
    private val runAsync: RunAsync,
    connectionUiMapper: ConnectionUiMapper,
) : ViewModel() {

    val state: StateFlow<FoundCityUi> = savedStateHandle.getStateFlow(KEY, FoundCityUi.Empty)
    val connection: StateFlow<ConnectionUi> = connectionUiMapper.state

    private val _event = MutableSharedFlow<FindCityEvent>()
    val event get() = _event.asSharedFlow()

    init {
        runAsync.debounce(
            scope = viewModelScope,
            start = { query ->
                if (query.isBlank())
                    FoundCityUi.Empty
                else
                    FoundCityUi.Loading
            },
            background = { query ->
                val foundCityResult = repository.findCity(query)
                val foundCityUi = foundCityResult.map(mapper)
                foundCityUi
            },
            ui = { foundCityUi ->
                savedStateHandle[KEY] = foundCityUi
            },
        )
    }

    fun findCity(
        cityName: String,
        isRetryCall: Boolean = false,
    ) = runAsync.emit(
        query = cityName,
        isRetryCall = isRetryCall
    )

    fun chooseCity(
        foundCity: FoundCity,
    ) = runAsync.runAsync(
        scope = viewModelScope,
        background = {
            repository.saveFoundCity(foundCity = foundCity)
        },
        ui = {
            _event.emit(FindCityEvent.NavigateToWeatherScreen)
        }
    )

    fun chooseLocation(
        latitude: Double,
        longitude: Double,
    ) = runAsync.runAsync(
        scope = viewModelScope,
        background = {
            repository.saveFoundCity(latitude, longitude)
        },
        ui = {
            _event.emit(FindCityEvent.NavigateToWeatherScreen)
        }
    )

    private companion object {
        const val KEY = "FoundCityUiKey"
    }
}