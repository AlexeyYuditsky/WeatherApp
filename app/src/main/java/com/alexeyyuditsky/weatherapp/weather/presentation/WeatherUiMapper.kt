package com.alexeyyuditsky.weatherapp.weather.presentation

import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import javax.inject.Inject

class WeatherUiMapper @Inject constructor(
    private val timeWrapper: TimeWrapper,
) : WeatherResult.Mapper<WeatherUi> {

    override fun mapToNoDataYet() = WeatherUi.Loading

    override fun mapToEmpty() = WeatherUi.Empty

    override fun mapToSuccess(
        weatherInCity: WeatherInCity,
    ) = WeatherUi.Success(
        cityName = weatherInCity.cityName,
        details = weatherInCity.details,
        imageUrl = weatherInCity.imageUrl,
        time = timeWrapper.getHumanReadableTime(weatherInCity.time),
        forecast = weatherInCity.forecast
    )
}