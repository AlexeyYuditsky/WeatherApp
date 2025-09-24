package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class FindCityPage(
    composeTestRule: ComposeContentTestRule,
) {

    private val findCityOutlinedTextField = composeTestRule.onNodeWithTag(
        testTag = "findCityOutlinedTextField",
    )

    private val foundCityButton = composeTestRule.onNodeWithTag(
        testTag = "foundCityButton",
        useUnmergedTree = true,
    )

    fun input(text: String) =
        findCityOutlinedTextField.performTextInput(text)

    fun assertCityFound(cityName: String) =
        foundCityButton.assertTextEquals(cityName)

    fun clickFoundCity(cityName: String) =
        foundCityButton.assertTextEquals(cityName).performClick()

}