package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import com.alexeyyuditsky.weatherapp.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherViewModelTest {

    private val weatherRepository = FakeWeatherRepository()
    private val fakeRunAsync = FakeRunAsync()
    private val savedStateHandle = SavedStateHandle()
    private val weatherViewModel = WeatherViewModel(
        savedStateHandle = savedStateHandle,
        weatherRepository = weatherRepository,
        runAsync = fakeRunAsync,
    )

    @Test
    fun getWeatherInCity() {
        val expected = WeatherUi.Empty
        val actual = weatherViewModel.state.value
        assertEquals(expected, actual)

        fakeRunAsync.returnResult()

        val expected2 = WeatherUi.Base(
            cityName = "Moscow city",
            temperature = "33.1°C",
        )
        val actual2 = weatherViewModel.state.value
        assertEquals(expected2, actual2)
    }

}

private class FakeWeatherRepository : WeatherRepository {

    override suspend fun fetchWeather(): WeatherInCity = WeatherInCity(
        cityName = "Moscow city",
        temperature = 33.1f,
    )

}