package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWeatherRepository(
    private val fakeRunAsync: FakeRunAsync
) : WeatherRepository {

    private val weatherFlow = MutableStateFlow(
        WeatherParams(-1f, 0f, "", 0, "", "")
    )
    private val errorFlow = MutableStateFlow(false)

    override fun fetchWeather(savedWeather: WeatherParams): WeatherResult {
        return if (savedWeather.latitude == -1f) {
            WeatherResult.NoDataYet
        } else {
            WeatherResult.Success(
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

    fun changeError(has: Boolean) {
        errorFlow.value = has
    }

    fun returnWeatherParams(params: WeatherParams) {
        fakeRunAsync.pingFlow(params)
    }

    var loadWeatherCalled = false

    override fun loadWeather() {
        loadWeatherCalled = true
    }
}