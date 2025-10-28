package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FindCityScreen(
    viewModel: FindCityViewModel,
    navigateToWeatherScreen: () -> Unit,
    onGetLocationClick: () -> Unit,
) {
    var input by rememberSaveable { mutableStateOf("") }
    val foundCityUi by viewModel.state.collectAsStateWithLifecycle()
    val close by viewModel.close.collectAsState()

    if (close) SideEffect {
        navigateToWeatherScreen.invoke()
    }

    FindCityScreenUi(
        input = input,
        onValueChange = { text ->
            viewModel.findCity(cityName = text)
            input = text
        },
        foundCityUi = foundCityUi,
        onFoundCityClick = { foundCity ->
            viewModel.chooseCity(foundCity = foundCity)
        },
        onRetryClick = {
            viewModel.findCity(cityName = input, isRetryCall = true)
        },
        onGetLocationClick = onGetLocationClick
    )
}