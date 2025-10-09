package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
) {
    val weatherScreenUi by viewModel.state.collectAsStateWithLifecycle()
    weatherScreenUi.Show(onRetryClick = viewModel::loadWeather)
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUiSuccess() = WeatherUi.Success(
    cityName = "Moscow city",
    temperature = "33.1°C",
).Show()

@Preview(showBackground = true)
@Composable
private fun PreviewWeatherScreenUiLoading() =
    WeatherUi.Loading.Show()

@Preview(showBackground = true)
@Composable
private fun PreviewWeatherScreenUiNoConnectionError() =
    WeatherUi.NoConnectionError.Show()