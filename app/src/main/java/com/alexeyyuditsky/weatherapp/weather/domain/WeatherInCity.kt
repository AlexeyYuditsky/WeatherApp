package com.alexeyyuditsky.weatherapp.weather.domain

data class WeatherInCity(
    val cityName: String,
    val details: String,
    val imageUrl: String,
    val time: Long,
    val forecast: List<Pair<String, String>> = emptyList()
)