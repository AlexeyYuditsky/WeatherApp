package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class WeatherPage(
    composeTestRule: ComposeContentTestRule,
) {

    private val cityNameText = composeTestRule.onNodeWithTag("cityNameText")
    private val temperatureText = composeTestRule.onNodeWithTag("temperatureText")
    private val noInternetConnectionText = composeTestRule.onNodeWithTag("noInternetConnectionText")
    private val retryButton = composeTestRule.onNodeWithTag("retryButton")
    private val circularProgress = composeTestRule.onNodeWithTag("circularProgress")

    fun assertCityName(cityName: String) =
        cityNameText.assertTextEquals(cityName)

    fun assertWeatherDisplayed(temperature: String) =
        temperatureText.assertTextEquals(temperature)

    fun assertNoConnectionIsDisplayed() {
        noInternetConnectionText.assertIsDisplayed()
        cityNameText.assertDoesNotExist()
        temperatureText.assertDoesNotExist()
    }

    fun clickRetry() =
        retryButton.performClick()

    fun assertLoading() =
        circularProgress.assertIsDisplayed()
}