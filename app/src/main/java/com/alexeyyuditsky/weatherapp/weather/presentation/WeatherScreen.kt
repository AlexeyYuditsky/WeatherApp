package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
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


@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUiBase() {
    WeatherScreenUi(
        ConnectedUi.Connected,
        ErrorUi.Empty,
        WeatherUi.Success("Moscow", "33.1°C", "", "5 min ago (10-Aug-2025)"),
        {}
    ) {}
}

@Preview(showBackground = true)
@Composable
fun PreviewLoading() {
    WeatherScreenUi(
        ConnectedUi.Connected,
        ErrorUi.Empty,
        WeatherUi.Loading,
        {}
    ) {}
}

@Preview(showBackground = true)
@Composable
fun PreviewDisconnected() {
    WeatherScreenUi(
        ConnectedUi.Disconnected,
        ErrorUi.Empty,
        WeatherUi.Success("Moscow", "33.1°C", "", "5 min ago (10-Aug-2025)"),
        {}
    ) {}
}

@Preview(showBackground = true)
@Composable
fun PreviewError() {
    WeatherScreenUi(
        ConnectedUi.Connected,
        ErrorUi.Error,
        WeatherUi.Empty,
        {}
    ) {}
}