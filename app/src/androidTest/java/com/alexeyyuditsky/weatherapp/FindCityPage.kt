package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement

class FindCityPage(
    composeTestRule: ComposeContentTestRule,
) {

    private val outlinedTextField = composeTestRule.onNodeWithTag("outlinedTextField")
    private val foundCityText = composeTestRule.onNodeWithTag("foundCityText", true)
    private val noInternetConnectionText = composeTestRule.onNodeWithTag("noInternetConnectionText")
    private val retryButton = composeTestRule.onNodeWithTag("retryButton")
    private val circularProgress = composeTestRule.onNodeWithTag("circularProgress")

    fun input(text: String) =
        outlinedTextField.performTextReplacement(text)

    fun assertCityFound(cityName: String) =
        foundCityText.assertTextEquals(cityName)

    fun clickFoundCity(cityName: String) =
        foundCityText.assertTextEquals(cityName).performClick()

    fun assertNoConnectionIsDisplayed() =
        noInternetConnectionText.assertIsDisplayed()

    fun clickRetry() =
        retryButton.performClick()

    fun assertEmptyIsDisplayed() {
        noInternetConnectionText.assertDoesNotExist()
        retryButton.assertDoesNotExist()
        foundCityText.assertDoesNotExist()
    }

    fun assertLoading() =
        circularProgress.assertIsDisplayed()
}