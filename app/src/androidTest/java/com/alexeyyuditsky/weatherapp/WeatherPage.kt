package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

class WeatherPage(
    composeTestRule: ComposeContentTestRule
) {

    private val cityNameNode = composeTestRule.onNodeWithTag("cityName")
    private val weatherTemperature = composeTestRule.onNodeWithTag("weatherTemperature")

    fun assertCityName(cityName: String) =
        cityNameNode.assertTextEquals(cityName)

    fun assertWeatherDisplayed(temperature: String) =
        weatherTemperature.assertTextEquals(temperature)

}