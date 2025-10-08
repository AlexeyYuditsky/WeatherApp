package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FindCityScreen(
    viewModel: FindCityViewModel,
    navigateToWeatherScreen: () -> Unit,
) {
    var input by rememberSaveable { mutableStateOf("") }
    val foundCityUi by viewModel.state.collectAsStateWithLifecycle()

    FindCityScreenUi(
        input = input,
        onInputChange = { text ->
            viewModel.inputFindCity(cityName = text)
            input = text
        },
        foundCityUi = foundCityUi,
        onFoundCityClick = { foundCity ->
            viewModel.chooseCity(foundCity = foundCity)
            navigateToWeatherScreen.invoke()
        },
        onRetryClick = { viewModel.retryFindCity(cityName = input) }
    )
}