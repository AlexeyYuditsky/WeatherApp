package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle

class WeatherViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
    private val runAsync: RunAsync,
) {



}