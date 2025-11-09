package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    val connection by viewModel.connection.collectAsStateWithLifecycle()
    var input by rememberSaveable { mutableStateOf("") }
    val foundCityUi by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is FindCityEvent.NavigateToWeatherScreen -> navigateToWeatherScreen.invoke()
            }
        }
    }

    FindCityScreenUi(
        connectionUi = connection,
        input = input,
        onValueChange = { text ->
            input = text
            viewModel.findCity(cityName = text)
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