package com.alexeyyuditsky.weatherapp.weather.domain

interface WeatherRepository {

    suspend fun fetchWeather(): WeatherInCity

}