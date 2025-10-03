package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import com.alexeyyuditsky.weatherapp.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherViewModelTest {

    private val repository = FakeWeatherRepository()
    private val fakeRunAsync = FakeRunAsync()
    private val viewModel = WeatherViewModel(
        savedStateHandle = SavedStateHandle(),
        repository = repository,
        runAsync = fakeRunAsync,
        mapper = WeatherUiMapper(),
    )

    @Test
    fun getErrorThenGetWeatherInCity() {
        assertEquals(WeatherUi.Empty, viewModel.state.value)

        fakeRunAsync.returnResult()
        assertEquals(WeatherUi.NoConnectionError, viewModel.state.value)

        viewModel.loadWeather()
        assertEquals(WeatherUi.NoConnectionError, viewModel.state.value)

        fakeRunAsync.returnResult()
        assertEquals(
            WeatherUi.Base(
                cityName = "Moscow city",
                temperature = "33.1°C",
            ),
            viewModel.state.value
        )
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