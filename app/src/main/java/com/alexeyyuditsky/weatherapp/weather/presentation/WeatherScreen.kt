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
    val connected by viewModel.connectionFlow.collectAsStateWithLifecycle(ConnectedUi.Connected)
    val error by viewModel.errorFlow.collectAsStateWithLifecycle(ErrorUi.Empty)

    WeatherScreenUi(
        connectedUi = connected,
        errorUi = error,
        weatherUi = weatherUi,
        retry = viewModel::retryLoadWeather,
        goToChooseLocation = goToChooseLocation,
    )
}