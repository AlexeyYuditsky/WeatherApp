package com.alexeyyuditsky.weatherapp.weather.presentation

import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import javax.inject.Inject

class WeatherUiMapper @Inject constructor() : WeatherResult.Mapper<WeatherUi> {

    override fun mapToSuccess(weatherInCity: WeatherInCity): WeatherUi =
        WeatherUi.Success(
            cityName = weatherInCity.cityName,
            temperature = weatherInCity.temperature.toString() + "°C"
        )

    override fun mapToLoading(): WeatherUi =
        WeatherUi.Loading

    override fun mapToEmpty(): WeatherUi =
        WeatherUi.Empty

    override fun mapToNoConnectionError(): WeatherUi =
        WeatherUi.NoConnectionError
}