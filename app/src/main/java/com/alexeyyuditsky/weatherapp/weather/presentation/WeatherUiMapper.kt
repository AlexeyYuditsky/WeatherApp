package com.alexeyyuditsky.weatherapp.weather.presentation

import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import javax.inject.Inject

class WeatherUiMapper @Inject constructor() : WeatherResult.Mapper<WeatherUi> {

    override fun mapBase(weatherInCity: WeatherInCity): WeatherUi =
        WeatherUi.Base(
            cityName = weatherInCity.cityName,
            temperature = weatherInCity.temperature.toString() + "°C"
        )

    override fun mapEmpty(): WeatherUi =
        WeatherUi.Empty

    override fun mapNoConnectionError(): WeatherUi =
        WeatherUi.NoConnectionError
}