package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement

class FindCityPage(
    composeTestRule: ComposeContentTestRule,
) {

    private val findCityOutlinedTextField = composeTestRule.onNodeWithTag(
        testTag = "findCityOutlinedTextField",
    )

    private val foundCityText = composeTestRule.onNodeWithTag(
        testTag = "foundCityText",
        useUnmergedTree = true,
    )

    fun input(text: String) =
        findCityOutlinedTextField.performTextReplacement(text)

    fun assertCityFound(cityName: String) =
        foundCityText.assertTextEquals(cityName)

    fun clickFoundCity(cityName: String) =
        foundCityText.assertTextEquals(cityName).performClick()

}