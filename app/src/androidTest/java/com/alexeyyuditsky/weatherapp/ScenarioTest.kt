package com.alexeyyuditsky.weatherapp

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
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityScreen
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityScreenUi
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityViewModel
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUi
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUiMapper
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherScreen
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUi
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUiMapper
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

    @Test
    fun findCityAndShowWeather() = with(composeTestRule) {
        val findCityViewModel = FindCityViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = FakeFindCityRepository(),
            runAsync = FakeRunAsync(),
            mapper = FoundCityUiMapper(),
        )
        val weatherViewModel = WeatherViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = FakeWeatherRepository(),
            runAsync = FakeRunAsync(),
            mapper = WeatherUiMapper(),
        )
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "findCityScreen",
            ) {
                composable(route = "findCityScreen") {
                    FindCityScreen(
                        viewModel = findCityViewModel,
                        navigateToWeatherScreen = { navController.navigate("weatherScreen") },
                    )
                }

                composable(route = "weatherScreen") {
                    WeatherScreen(
                        viewModel = weatherViewModel,
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
                    val shouldShowNoConnectionError = rememberSaveable { mutableStateOf(true) }

                    FindCityScreenUi(
                        input = input.value,
                        onInputChange = { text: String -> input.value = text },
                        foundCityUi = if (input.value.isEmpty())
                            FoundCityUi.Empty
                        else if (input.value == "Mo")
                            if (shouldShowNoConnectionError.value)
                                FoundCityUi.NoConnectionError
                            else
                                FoundCityUi.Empty
                        else
                            FoundCityUi.Base(
                                foundCity = FoundCity(
                                    name = "Moscow",
                                    latitude = 55.75f,
                                    longitude = 37.61f,
                                )
                            ),
                        onFoundCityClick = { navController.navigate("weatherScreen") },
                        onRetryClick = { shouldShowNoConnectionError.value = false },
                    )
                }

                composable(route = "weatherScreen") {
                    val shouldShowNoConnectionError = rememberSaveable { mutableStateOf(true) }

                    if (shouldShowNoConnectionError.value)
                        WeatherUi.NoConnectionError.Show(
                            onRetryClick = {
                                shouldShowNoConnectionError.value = false
                            }
                        )
                    else
                        WeatherUi.Base(
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
        findCityPage.input(text = "Mo")
        findCityPage.assertNoConnectionIsDisplayed()
        findCityPage.clickRetry()
        findCityPage.assertEmptyIsDisplayed()
        findCityPage.input(text = "Mos")
        findCityPage.assertCityFound(cityName = "Moscow")
        findCityPage.clickFoundCity(cityName = "Moscow")

        val weatherPage = WeatherPage(composeTestRule = composeTestRule)
        weatherPage.assertNoConnectionIsDisplayed()
        weatherPage.clickRetry()
        weatherPage.assertCityName(cityName = "Moscow city")
        weatherPage.assertWeatherDisplayed(temperature = "33.1°C")
    }
}

private class FakeFindCityRepository : FindCityRepository {

    private var shouldShowError = true

    override suspend fun findCity(query: String): FoundCityResult = when {
        query.isBlank() ->
            error("repository should not accept empty query")

        query == "Mo" -> {
            if (shouldShowError)
                FoundCityResult.Error(error = NoInternetException)
                    .also { shouldShowError = false }
            else
                FoundCityResult.Empty
        }

        query == "Mos" ->
            FoundCityResult.Base(
                foundCity = FoundCity(
                    name = "Moscow",
                    latitude = 55.75f,
                    longitude = 37.61f,
                )
            )

        else ->
            error("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(
                name = "Moscow",
                latitude = 55.75f,
                longitude = 37.61f,
            )
        ) throw IllegalStateException("save called with wrong argument $foundCity")
    }
}

private class FakeWeatherRepository : WeatherRepository {

    private var shouldShowError = true

    override suspend fun fetchWeather(): WeatherResult = if (shouldShowError)
        WeatherResult.Error(error = NoInternetException)
            .also { shouldShowError = false }
    else
        WeatherResult.Base(
            weatherInCity = WeatherInCity(
                cityName = "Moscow city",
                temperature = 33.1f,
            )
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