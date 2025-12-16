package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherInCity
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val minutes: Int,
    private val cacheDataSource: WeatherCacheDataSource,
    private val cloudDataSource: WeatherCloudDataSource,
    private val startForegroundWrapper: StartForegroundWrapper,
) : WeatherRepository {

    override fun fetchWeather(savedWeather: WeatherParams): WeatherResult {
        val (latitude, longitude) = cacheDataSource.cityParams

        val invalidSavedWeather = savedWeather.isEmpty() || !savedWeather.same(latitude, longitude)

        return if (invalidSavedWeather) {
            loadWeather()
            WeatherResult.NoDataYet
        } else {
            val now = System.currentTimeMillis()
            val lastUpdate = savedWeather.time
            val refreshIntervalMs = minutes * 60 * 1000
            val timePassed = now - lastUpdate
            val needRefresh = timePassed > refreshIntervalMs
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

    override suspend fun refreshWeather() = coroutineScope {
        cacheDataSource.saveHasError(hasError = false)
        val (latitude, longitude) = cacheDataSource.cityParams

        val weatherDeferred = async {
            cloudDataSource.weather(latitude, longitude)
        }

        val airPollutionDeferred = async {
            try {
                val airPollutionCloud = cloudDataSource.airPollution(latitude, longitude)
                airPollutionCloud.list.firstOrNull()?.main?.ui() ?: ""
            } catch (_: IOException) {
                "" // ignore air pollution if error
            }
        }

        val forecastDeferred = async {
            try {
                val response = cloudDataSource.forecast(latitude, longitude)
                response.list.map {
                    it.details(response.city.timezone)
                }
            } catch (_: IOException) {
                emptyList() // ignore forecast if error
            }
        }

        val weather = weatherDeferred.await()
        val airPollution = airPollutionDeferred.await()
        val forecast = forecastDeferred.await()

        val now = System.currentTimeMillis()
        val (details, imageUrl) = weather.details()

        cacheDataSource.saveWeather(
            WeatherParams(
                latitude = latitude,
                longitude = longitude,
                city = weather.cityName,
                time = now,
                imageUrl = imageUrl,
                details = details + airPollution,
                forecast = forecast,
            )
        )
    }

    override suspend fun saveException(exception: DomainException) =
        cacheDataSource.saveHasError(hasError = true)

    override val cachedWeatherFlow: Flow<WeatherParams> =
        cacheDataSource.cachedWeatherFlow

    override val hasErrorFlow: Flow<Boolean> =
        cacheDataSource.hasErrorFlow

    override fun loadWeather() =
        startForegroundWrapper.start()
}