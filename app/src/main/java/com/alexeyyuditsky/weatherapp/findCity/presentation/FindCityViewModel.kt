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

    val state = savedStateHandle.getStateFlow(KEY, mapper.mapEmpty())

    fun findCity(cityName: String) = if (cityName.isBlank())
        savedStateHandle[KEY] = mapper.mapEmpty()
    else
        runAsync.invoke(
            scope = viewModelScope,
            background = {
                val foundCityResult = repository.findCity(query = cityName)
                val foundCityUi = foundCityResult.map(mapper = mapper)
                foundCityUi
            },
            ui = { foundCityUiBase ->
                savedStateHandle[KEY] = foundCityUiBase
            }
        )

    fun chooseCity(foundCity: FoundCity) = runAsync.invoke(
        scope = viewModelScope,
        background = {
            repository.saveCity(foundCity = foundCity)
        }
    )

    private companion object {
        const val KEY = "FoundCityUiKey"
    }

}