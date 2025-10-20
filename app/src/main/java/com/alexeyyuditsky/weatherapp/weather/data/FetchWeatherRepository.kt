package com.alexeyyuditsky.weatherapp.weather.data

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import javax.inject.Inject

interface FetchWeatherRepository {

    suspend fun fetchWeather()

    suspend fun saveException(e: DomainException)

    class Base @Inject constructor(
        private val cloudDataSource: WeatherCloudDataSource,
        private val cacheDataSource: WeatherCacheDataSource,
    ) : FetchWeatherRepository {

        override suspend fun fetchWeather() {
            cacheDataSource.saveHasError(hasError = false)
            val (lat, lon) = cacheDataSource.cityParams()
            val weatherCloud = cloudDataSource.weather(lat, lon)
            val airPollution = try {
                val airPollutionCloud = cloudDataSource.airPollution(lat, lon)
                airPollutionCloud.list.firstOrNull()?.main?.ui() ?: ""
            } catch (_: Exception) {
                ""//ignore air pollution if error
            }
            val forecast = try {
                val response = cloudDataSource.forecast(lat, lon)
                response.list.map {
                    it.details(response.city.timezone)
                }
            } catch (_: Exception) {
                emptyList()//ignore forecast if error
            }
            val now = System.currentTimeMillis()
            val (details, imageUrl) = weatherCloud.details()
            cacheDataSource.saveWeather(
                WeatherParams(
                    latitude = lat,
                    longitude = lon,
                    city = weatherCloud.cityName,
                    time = now,
                    imageUrl = imageUrl,
                    details = details + airPollution,
                    forecast = forecast,
                )
            )
        }

        override suspend fun saveException(e: DomainException) {
            cacheDataSource.saveHasError(hasError = true)
        }
    }
}