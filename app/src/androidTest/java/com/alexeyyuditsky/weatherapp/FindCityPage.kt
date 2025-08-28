package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class FindCityPage(
    composeTestRule: ComposeContentTestRule
) {

    private val findCityInputField = composeTestRule.onNodeWithTag("findCityInputField")
    private val foundCityUi = composeTestRule.onNodeWithTag("foundCityUi")

    fun input(text: String) =
        findCityInputField.performTextInput(text)

    fun assertCityFound(cityName: String) =
        foundCityUi.assertTextEquals(cityName)

    fun clickFoundCity(cityName: String) =
        foundCityUi.assertTextEquals(cityName).performClick()

}