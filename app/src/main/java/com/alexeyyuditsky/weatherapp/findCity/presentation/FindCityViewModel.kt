package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindCityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val findCityRepository: FindCityRepository,
    private val runAsync: RunAsync,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow(KEY, FoundCityUi.Empty)

    fun findCity(cityName: String) = runAsync.invoke(
        scope = viewModelScope,
        background = {
            val foundCity = findCityRepository.findCity(query = cityName)
            FoundCityUi.Base(foundCity = foundCity)
        },
        ui = { foundCityUiBase ->
            savedStateHandle[KEY] = foundCityUiBase
        }
    )

    fun chooseCity(foundCity: FoundCity) = runAsync.invoke(
        scope = viewModelScope,
        background = {
            findCityRepository.saveCity(foundCity = foundCity)
        }
    )

    private companion object {
        const val KEY = "FoundCityUiKey"
    }

}