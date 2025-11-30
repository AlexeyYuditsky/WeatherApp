package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val minutes: Int,
    private val cacheDataSource: WeatherCacheDataSource,
    private val startForegroundWrapper: StartForegroundWrapper,
) : WeatherRepository {

    override fun fetchWeather(savedWeather: WeatherParams): WeatherResult {
        val (latitude, longitude) = cacheDataSource.cityParams()
        val invalidSavedWeather = savedWeather.isEmpty() || !savedWeather.same(latitude, longitude)
        return if (invalidSavedWeather) {
            loadWeather()
            WeatherResult.NoDataYet
        } else {
            val needRefresh = System.currentTimeMillis() - savedWeather.time > minutes * 60 * 1000
            if (needRefresh)
                loadWeather()
            WeatherResult.Success(
                weatherInCity = WeatherInCity(
                    cityName = savedWeather.city,
                    details = savedWeather.details,
                    imageUrl = savedWeather.imageUrl,
                    time = savedWeather.time,
                    forecast = savedWeather.forecast
                )
            )
        }
    }

    override val weatherFlow: Flow<WeatherParams> = cacheDataSource.savedWeather()

    override val errorFlow: Flow<Boolean> = cacheDataSource.hasError()

    override fun loadWeather() = startForegroundWrapper.start()
}