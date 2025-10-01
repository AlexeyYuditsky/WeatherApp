package com.alexeyyuditsky.weatherapp.weather.presentation

import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import javax.inject.Inject

class WeatherUiMapper @Inject constructor() : WeatherResult.Mapper<WeatherUi> {

    override fun mapWeatherInCity(weatherInCity: WeatherInCity): WeatherUi =
        WeatherUi.Base(
            cityName = weatherInCity.cityName,
            temperature = weatherInCity.temperature.toString() + "°C"
        )

    override fun mapNoConnection(): WeatherUi =
        WeatherUi.NoConnectionError

    override fun mapEmpty(): WeatherUi =
        WeatherUi.Empty

}