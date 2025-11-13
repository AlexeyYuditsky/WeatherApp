package com.alexeyyuditsky.weatherapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexeyyuditsky.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.core.Connection
import com.alexeyyuditsky.weatherapp.core.Screen
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityScreen
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityViewModel
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUiMapper
import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import com.alexeyyuditsky.weatherapp.weather.presentation.TimeWrapper
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherScreen
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUiMapper
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val fakeRunAsync = FakeRunAsync()
    val fakeWeatherRepository = FakeWeatherRepository()
    val fakeConnection = FakeConnection()

    @Test
    fun findCityAndShowWeather(): Unit = with(composeTestRule) {
        val findCityViewModel = FindCityViewModel(
            mapper = FoundCityUiMapper(),
            savedStateHandle = SavedStateHandle(),
            repository = FakeFindCityRepository(),
            runAsync = fakeRunAsync
        )
        val weatherViewModel = WeatherViewModel(
            mapper = WeatherUiMapper(TimeWrapper.Base()),
            savedStateHandle = SavedStateHandle(),
            repository = fakeWeatherRepository,
            runAsync = fakeRunAsync,
            connection = fakeConnection
        )
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.FindCity) {
                composable<Screen.FindCity> {
                    FindCityScreen(
                        viewModel = findCityViewModel,
                        navigateToWeatherScreen = {
                            navController.navigate(Screen.Weather)
                        }
                    ) {
                        throw IllegalStateException("choose location is not tested here")
                    }
                }

                composable<Screen.Weather> {
                    WeatherScreen(
                        viewModel = weatherViewModel
                    ) {
                        throw IllegalStateException("go back to choose location is not tested here")
                    }
                }
            }
        }

        startUiTestWithLoading()
    }

    private fun startUiTestWithLoading() {
        val findCityPage = FindCityPage(composeTestRule = composeTestRule)
        findCityPage.input(text = "Mo")
        fakeRunAsync.returnResult()
        findCityPage.assertNoConnectionIsDisplayed()

        findCityPage.clickRetry()
        fakeRunAsync.returnResult()
        findCityPage.assertEmptyResult()

        findCityPage.input(text = "Mos")
        findCityPage.assertLoading()
        fakeRunAsync.returnResult()
        findCityPage.assertCityFound(cityName = "Moscow")

        findCityPage.clickFoundCity(cityName = "Moscow")
        val weatherPage = WeatherPage(composeTestRule = composeTestRule)
        fakeRunAsync.returnResult()
        weatherPage.assertNoConnectionIsDisplayed()

        fakeConnection.changeConnected(true)
        weatherPage.assertLoading()
        fakeWeatherRepository.loadWeather()
        weatherPage.assertCityName(cityName = "Moscow city")
        weatherPage.assertWeatherDisplayed(temp = "33.1")
    }
}

class FakeWeatherRepository : WeatherRepository {

    private var shouldShowError = true
    private val weatherFlow = MutableStateFlow(WeatherParams(0f, 0f, "", 0, "", ""))
    private val errorFlow = MutableStateFlow(false)

    override fun weather(savedWeather: WeatherParams): WeatherResult {
        if (shouldShowError) {
            shouldShowError = false
            return WeatherResult.NoDataYet
        } else {
            return WeatherResult.Success(
                weatherInCity = WeatherInCity(
                    cityName = "Moscow city",
                    details = "33.1",
                    imageUrl = "",
                    time = 0
                )
            )
        }
    }

    override fun weatherFlow(): Flow<WeatherParams> {
        return weatherFlow
    }

    override fun errorFlow(): Flow<Boolean> {
        return errorFlow
    }

    override fun loadWeather() {
        weatherFlow.value = WeatherParams(5f, 5f, "moscow", 1, "", "")
    }
}

class FakeFindCityRepository : FindCityRepository {

    private var shouldShowError = true

    override suspend fun findCity(query: String): FoundCityResult {
        if (query.trim().isEmpty())
            throw IllegalStateException("repository should not accept empty query")
        if (query == "Mo") {
            if (shouldShowError) {
                shouldShowError = false
                return FoundCityResult.Error(error = NoInternetException)
            } else {
                return FoundCityResult.Empty
            }
        }

        if (query == "Mos")
            return FoundCityResult.Success(
                foundCity = FoundCity(
                    name = "Moscow",
                    latitude = 55.75f,
                    longitude = 37.61f
                )
            )

        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveFoundCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(name = "Moscow", latitude = 55.75f, longitude = 37.61f))
            throw IllegalStateException("save called with wrong argument $foundCity")
    }

    override suspend fun saveFoundCity(latitude: Double, longitude: Double) {
        throw IllegalStateException("choose location is not tested here")
    }
}

class FakeConnection : Connection {

    private val mutableStateFlow = MutableStateFlow(false)
    override val statuses: StateFlow<Boolean> get() = mutableStateFlow.asStateFlow()

    fun changeConnected(connected: Boolean) {
        mutableStateFlow.value = connected
    }
}