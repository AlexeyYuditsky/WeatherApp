package com.alexeyyuditsky.weatherapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexeyyuditsky.weatherapp.core.Routes.FIND_CITY
import com.alexeyyuditsky.weatherapp.core.Routes.WEATHER
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val fakeRunAsync = FakeRunAsync()

    @Test
    fun findCityAndShowWeather() {
        with(composeTestRule) {
            val findCityViewModel = FindCityViewModel(
                savedStateHandle = SavedStateHandle(),
                repository = FakeFindCityRepository(),
                runAsync = fakeRunAsync,
                mapper = FoundCityUiMapper(),
            )
            val weatherViewModel = WeatherViewModel(
                savedStateHandle = SavedStateHandle(),
                repository = FakeWeatherRepository(),
                runAsync = fakeRunAsync,
                mapper = WeatherUiMapper(),
            )
            setContent {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = FIND_CITY,
                ) {
                    composable(route = FIND_CITY) {
                        FindCityScreen(
                            viewModel = findCityViewModel,
                            navigateToWeatherScreen = { navController.navigate(WEATHER) },
                        )
                    }

                    composable(route = WEATHER) {
                        WeatherScreen(
                            viewModel = weatherViewModel,
                        )
                    }
                }
            }

            startUiTestWithLoading()
        }
    }

    @Test
    fun findCityAndShowWeatherUi() = with(composeTestRule) {
        val fakeRunAsyncUi = FakeRunAsyncUi()
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = FIND_CITY,
            ) {
                composable(route = FIND_CITY) {
                    val input = rememberSaveable { mutableStateOf("") }
                    val shouldShowNoConnectionError = rememberSaveable { mutableStateOf(true) }
                    val showLoadingText =
                        fakeRunAsyncUi.stateFlow.collectAsStateWithLifecycle().value

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
                            when (showLoadingText) {
                                "loadMoscow" ->
                                    FoundCityUi.Loading

                                "moscowLoaded" ->
                                    FoundCityUi.Success(
                                        foundCity = FoundCity(
                                            name = "Moscow",
                                            latitude = 55.75f,
                                            longitude = 37.61f,
                                        )
                                    )

                                else ->
                                    FoundCityUi.Empty
                            },
                        onFoundCityClick = { navController.navigate(WEATHER) },
                        onRetryClick = { shouldShowNoConnectionError.value = false },
                    )
                }

                composable(route = WEATHER) {
                    val shouldShowNoConnectionError = rememberSaveable { mutableStateOf(true) }
                    val showLoadingText =
                        fakeRunAsyncUi.stateFlow.collectAsStateWithLifecycle().value

                    if (shouldShowNoConnectionError.value)
                        WeatherUi.NoConnectionError.Show(
                            onRetryClick = { shouldShowNoConnectionError.value = false }
                        )
                    else {
                        if (showLoadingText == "loadWeather")
                            WeatherUi.Loading.Show()
                        else if (showLoadingText == "weatherLoaded")
                            WeatherUi.Success(
                                cityName = "Moscow city",
                                temperature = "33.1°C",
                            ).Show()
                    }
                }
            }
        }

        startUiTest(fakeRunAsyncUi)
    }

    private fun startUiTestWithLoading() {
        val findCityPage = FindCityPage(composeTestRule = composeTestRule)
        findCityPage.input(text = "Mo")
        fakeRunAsync.returnResult()
        findCityPage.assertNoConnectionIsDisplayed()

        findCityPage.clickRetry()
        fakeRunAsync.returnResult()
        findCityPage.assertEmptyIsDisplayed()

        findCityPage.input(text = "Mos")
        findCityPage.assertLoading()
        fakeRunAsync.returnResult()
        findCityPage.assertCityFound(cityName = "Moscow")

        findCityPage.clickFoundCity(cityName = "Moscow")

        val weatherPage = WeatherPage(composeTestRule = composeTestRule)
        fakeRunAsync.returnResult()
        weatherPage.assertNoConnectionIsDisplayed()

        weatherPage.clickRetry()
        weatherPage.assertLoading()
        fakeRunAsync.returnResult()
        weatherPage.assertCityName(cityName = "Moscow city")
        weatherPage.assertWeatherDisplayed(temperature = "33.1°C")
    }

    private fun startUiTest(fakeRunAsyncUi: FakeRunAsyncUi) {
        val findCityPage = FindCityPage(composeTestRule = composeTestRule)
        findCityPage.input(text = "Mo")
        findCityPage.assertNoConnectionIsDisplayed()

        findCityPage.clickRetry()
        findCityPage.assertEmptyIsDisplayed()

        findCityPage.input(text = "Mos")
        fakeRunAsyncUi.change("loadMoscow")
        findCityPage.assertLoading()
        fakeRunAsyncUi.change("moscowLoaded")
        findCityPage.assertCityFound(cityName = "Moscow")

        findCityPage.clickFoundCity(cityName = "Moscow")

        val weatherPage = WeatherPage(composeTestRule = composeTestRule)
        weatherPage.assertNoConnectionIsDisplayed()
        weatherPage.clickRetry()
        fakeRunAsyncUi.change("loadWeather")
        weatherPage.assertLoading()
        fakeRunAsyncUi.change("weatherLoaded")
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
            FoundCityResult.Success(
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
        WeatherResult.Success(
            weatherInCity = WeatherInCity(
                cityName = "Moscow city",
                temperature = 33.1f,
            )
        )
}

private class FakeRunAsyncUi {
    private val mutableStateFlow = MutableStateFlow("")
    val stateFlow = mutableStateFlow.asStateFlow()

    fun change(value: String) {
        mutableStateFlow.value = value
    }
}

class FakeRunAsync : RunAsync {

    private var resultCached: Any? = null
    private var uiCached: (Any) -> Unit = {}

    override fun <T : Any> run(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    ) = runBlocking {
        val result: T = background.invoke()
        resultCached = result
        @Suppress("UNCHECKED_CAST")
        uiCached = ui as (Any) -> Unit
    }

    private var backgroundDebounced: suspend (String) -> Any = {}
    private var uiDebounced: (Any) -> Unit = {}
    private var debouncedResult: Any? = null

    override fun <T : Any> debounce(
        scope: CoroutineScope,
        background: suspend (String) -> T,
        ui: (T) -> Unit,
    ) {
        backgroundDebounced = background
        @Suppress("UNCHECKED_CAST")
        uiDebounced = ui as (Any) -> Unit
    }

    override fun emitInput(query: String) = runBlocking {
        debouncedResult = backgroundDebounced.invoke(query)
    }

    override fun emitRetry(query: String) = runBlocking {
        debouncedResult = backgroundDebounced.invoke(query)
    }

    fun returnResult() {
        resultCached?.let {
            uiCached.invoke(it)
            resultCached = null
        }
        debouncedResult?.let {
            uiDebounced.invoke(it)
            debouncedResult = null
        }
    }

}