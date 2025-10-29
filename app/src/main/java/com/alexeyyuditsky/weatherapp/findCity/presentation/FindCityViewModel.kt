package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class FindCityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FindCityRepository,
    private val mapper: FoundCityResult.Mapper<FoundCityUi>,
    private val runAsync: RunAsync,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow<FoundCityUi>(KEY, FoundCityUi.Empty)

    private val _events = MutableSharedFlow<FindCityEvent>(extraBufferCapacity = 1)
    val events get() = _events.asSharedFlow()

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
                foundCityResult.map(mapper)
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
            _events.tryEmit(FindCityEvent.NavigateToWeatherScreen)
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
            _events.tryEmit(FindCityEvent.NavigateToWeatherScreen)
        }
    )

    private companion object {
        const val KEY = "FoundCityUiKey"
    }
}