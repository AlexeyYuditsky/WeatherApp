package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement

class FindCityPage(
    private val composeTestRule: ComposeContentTestRule,
) {

    private val inputField = composeTestRule.onNodeWithTag("outlinedTextField")
    private val foundCityUi = composeTestRule.onNodeWithTag("foundCityText", useUnmergedTree = true)
    private val noConnectionError = composeTestRule.onNodeWithTag("noInternetConnection")
    private val retryButton = composeTestRule.onNodeWithTag("retryButton")
    private val loading = composeTestRule.onNodeWithTag("circularProgress")

    fun input(text: String) {
        inputField.performTextReplacement(text)
    }

    fun assertCityFound(cityName: String) {
        foundCityUi.assertTextEquals(cityName)
    }

    fun clickFoundCity(cityName: String) {
        composeTestRule.onNodeWithText(cityName).performClick()
    }

    fun assertNoConnectionIsDisplayed() {
        noConnectionError.assertIsDisplayed()
    }

    fun clickRetry() {
        retryButton.performClick()
    }

    fun assertEmptyResult() {
        noConnectionError.assertDoesNotExist()
        retryButton.assertDoesNotExist()
        foundCityUi.assertDoesNotExist()
    }

    fun assertLoading() {
        loading.assertIsDisplayed()
    }
}