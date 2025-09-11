package com.alexeyyuditsky.weatherapp

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityScreen
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityScreenUi
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityViewModel
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUi
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherScreen
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherScreenUi
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @SuppressLint("ViewModelConstructorInComposable")
    @Test
    fun findCityAndShowWeather() = with(composeTestRule) {
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "findCityScreen",
            ) {
                composable(route = "findCityScreen") {
                    FindCityScreen(
                        viewModel = FindCityViewModel(
                            savedStateHandle = SavedStateHandle(),
                            findCityRepository = FakeFindCityRepository(),
                            runAsync = FakeRunAsync(),
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
                            weatherRepository = FakeWeatherRepository(),
                            runAsync = FakeRunAsync(),
                        )
                    )
                }
            }
        }

        startUiTest()
    }

    @Test
    fun findCityAndShowWeatherUi() = with(composeTestRule) {
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "findCityScreen",
            ) {
                composable(route = "findCityScreen") {
                    val input = rememberSaveable { mutableStateOf("") }

                    FindCityScreenUi(
                        input = input.value, onInputChange = { text: String ->
                            input.value = text
                        },
                        foundCityUi = if (input.value.isEmpty())
                            FoundCityUi.Empty
                        else
                            FoundCityUi.Base(
                                foundCity = FoundCity(
                                    name = "Moscow",
                                    latitude = 55.75,
                                    longitude = 37.61,
                                )
                            ),
                        onFoundCityClick = { foundCity: FoundCity ->
                            navController.navigate("weatherScreen")
                        }
                    )
                }

                composable(route = "weatherScreen") {
                    WeatherScreenUi.Base(
                        cityName = "Moscow city",
                        temperature = "33.1°C",
                    ).Show()
                }
            }

        }

        startUiTest()
    }

    private fun startUiTest() {
        val findCityPage = FindCityPage(composeTestRule = composeTestRule)
        findCityPage.input(text = "Mos")
        findCityPage.assertCityFound(cityName = "Moscow")
        findCityPage.clickFoundCity(cityName = "Moscow")

        val weatherPage = WeatherPage(composeTestRule = composeTestRule)
        weatherPage.assertCityName(cityName = "Moscow city")
        weatherPage.assertWeatherDisplayed(temperature = "33.1°C")
    }
}

private class FakeFindCityRepository : FindCityRepository {

    override suspend fun findCity(query: String): FoundCity {
        if (query == "Mos") return FoundCity(
            name = "Moscow",
            latitude = 55.75,
            longitude = 37.61,
        )

        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(
                name = "Moscow",
                latitude = 55.75,
                longitude = 37.61,
            )
        ) throw IllegalStateException("save called with wrong argument $foundCity")
    }

}

private class FakeWeatherRepository : WeatherRepository {

    override suspend fun fetchWeather(): WeatherInCity = WeatherInCity(
        cityName = "Moscow city",
        temperature = 33.1,
    )

}

private class FakeRunAsync : RunAsync {

    override fun <T : Any> invoke(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    ) = runTest {
        val result: T = background.invoke()
        ui.invoke(result)
    }

}