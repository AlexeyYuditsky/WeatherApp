package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun findCityAndShowWeather() = with(composeTestRule) {
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "findCityScreen"
            ) {
                composable(route = "findCityScreen") {
                    FindCityScreen(
                        viewModel = findCityViewModel(
                            savedStateHandle = SavedStateHandle(),
                            repository = FakeFindCityRepository()
                        ),
                        navigateToWeatherScreen = {
                            navController.navigate("weatherScreen")
                        }
                    )
                }

                composable(route = "weatherScreen") {
                    WeatherScreen(
                        viewModel = WeatherViewModel(
                            savedStateHandle = SavedStateHandle(),
                            repository = FakeWeatherRepository()
                        )
                    )
                }
            }

            val findCityPage = FindCityPage(composeTestRule = composeTestRule)
            findCityPage.input(text = "Mos")
            findCityPage.assertCityFound(cityName = "Moscow")
            findCityPage.clickFoundCity(cityName = "Moscow")

            val weatherPage = WeatherPage(composeTestRule = composeTestRule)
            weatherPage.assertCityName(cityName = "Moscow city")
            weatherPage.assertWeatherDisplayed(temperature = "33°C")
        }
    }
}

private class FakeFindCityRepository : FindCityRepository {

    override suspend fun findCity(query: String): FoundCity {
        if (query == "Mos")
            return FoundCity(
                name = "Moscow",
                latitude = 55.7504461,
                longitude = 37.6174943,
            )

        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(
                name = "Moscow",
                latitude = 55.7504461,
                longitude = 37.6174943,
            )
        )
            throw IllegalStateException("save called with wrong argument $foundCity")
    }

}

private class FakeWeatherRepository : WeatherRepository {

    override suspend fun fetchWeather(): WeatherInCity =
        WeatherInCity(
            cityName = "Moscow city",
            temperature = "33"
        )

}