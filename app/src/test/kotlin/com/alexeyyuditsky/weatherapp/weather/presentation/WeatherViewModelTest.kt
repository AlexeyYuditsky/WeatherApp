package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import com.alexeyyuditsky.weatherapp.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.weather.data.FakeWeatherRepository
import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherViewModelTest {

    private val fakeRunAsync = FakeRunAsync()
    private val repository = FakeWeatherRepository(fakeRunAsync = fakeRunAsync)
    private val viewModel = WeatherViewModel(
        savedStateHandle = SavedStateHandle(),
        repository = repository,
        runAsync = fakeRunAsync,
        mapper = WeatherUiMapper(FakeTimeWrapper()),
        connection = FakeConnection()
    )

    @Test
    fun loadingThenGetWeatherInCity() {
        val state: StateFlow<WeatherUi> = viewModel.state
        assertEquals(WeatherUi.Empty, state.value)

        repository.returnWeatherParams(WeatherParams(-1f, 20f, "moscow", 10, "htps", "somedetails"))
        assertEquals(WeatherUi.Loading, state.value)

        repository.returnWeatherParams(WeatherParams(15f, 20f, "moscow", 10, "htps", "somedetails"))
        assertEquals(
            WeatherUi.Success(
                cityName = "Moscow city",
                details = "33.1",
                imageUrl = "",
                time = "0",
            ), viewModel.state.value
        )
    }

    @Test
    fun loadingWeather() {
        assertEquals(WeatherUi.Empty, viewModel.state.value)

        viewModel.retryLoadWeather()
        assertEquals(WeatherUi.Loading, viewModel.state.value)

        fakeRunAsync.returnResult()
        assertEquals(true, repository.loadWeatherCalled)
    }
}