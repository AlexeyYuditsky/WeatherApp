package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

class WeatherPage(
    composeTestRule: ComposeContentTestRule,
) {

    private val cityNameNode = composeTestRule.onNodeWithTag("CityName")
    private val weatherTemperature = composeTestRule.onNodeWithTag("WeatherTemperature")
    private val noConnectionError = composeTestRule.onNodeWithTag("noInternetConnection")
    private val loading = composeTestRule.onNodeWithTag("circularProgress")

    fun assertCityName(cityName: String) {
        cityNameNode.assertTextEquals(cityName)
    }

    fun assertWeatherDisplayed(temp: String) {
        weatherTemperature.assertTextEquals(temp)
    }

    fun assertNoConnectionIsDisplayed() {
        noConnectionError.assertIsDisplayed()
        cityNameNode.assertDoesNotExist()
        weatherTemperature.assertDoesNotExist()
    }

    fun assertLoading() {
        loading.assertIsDisplayed()
    }
}