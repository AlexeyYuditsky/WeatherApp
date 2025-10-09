package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindCityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FindCityRepository,
    private val runAsync: RunAsync,
    private val mapper: FoundCityResult.Mapper<FoundCityUi>,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow(KEY, mapper.mapToEmpty())

    init {
        runAsync.debounce(
            scope = viewModelScope,
            background = { latestQuery ->
                if (latestQuery.isBlank())
                    mapper.mapToEmpty()
                else {
                    savedStateHandle[KEY] = mapper.mapToLoading()
                    val foundCityResult = repository.findCity(latestQuery)
                    foundCityResult.map(mapper)
                }
            },
            ui = { foundCityUi ->
                savedStateHandle[KEY] = foundCityUi
            }
        )
    }

    fun findCity(
        cityName: String,
        isRetryCall: Boolean = false,
    ) = runAsync.emit(
        query = cityName,
        isRetryCall = isRetryCall
    )

    fun chooseCity(foundCity: FoundCity) = runAsync.run(
        scope = viewModelScope,
        background = { repository.saveCity(foundCity = foundCity) }
    )

    private companion object {
        const val KEY = "FoundCityUiKey"
    }
}