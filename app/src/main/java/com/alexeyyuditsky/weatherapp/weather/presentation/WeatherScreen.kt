package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
) {
    val weatherScreenUi = viewModel.state.collectAsStateWithLifecycle()
    weatherScreenUi.value.Show()
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUi() = WeatherUi.Base(
    cityName = "Moscow city",
    temperature = "33.1°C",
).Show()