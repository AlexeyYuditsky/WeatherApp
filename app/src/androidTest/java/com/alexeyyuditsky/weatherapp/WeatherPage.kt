package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

class WeatherPage(
    composeTestRule: ComposeContentTestRule
) {

    private val cityNameText = composeTestRule.onNodeWithTag(testTag = "cityNameText")
    private val temperatureText = composeTestRule.onNodeWithTag(testTag = "temperatureText")

    fun assertCityName(cityName: String) =
        cityNameText.assertTextEquals(cityName)

    fun assertWeatherDisplayed(temperature: String) =
        temperatureText.assertTextEquals(temperature)

}