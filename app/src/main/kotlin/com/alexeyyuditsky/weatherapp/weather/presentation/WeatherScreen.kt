package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    goToChooseLocation: () -> Unit,
) {
    val weatherUi by viewModel.state.collectAsStateWithLifecycle()
    val connection by viewModel.connection.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle(ErrorUi.Empty)

    WeatherScreenUi(
        connectionUi = connection,
        errorUi = error,
        weatherUi = weatherUi,
        retry = viewModel::retryLoadWeather,
        goToChooseLocation = goToChooseLocation,
    )
}