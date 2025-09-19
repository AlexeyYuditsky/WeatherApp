package com.alexeyyuditsky.weatherapp.findCity.domain

import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCacheDataSource
import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCloudDataSource
import javax.inject.Inject
import kotlin.String

interface FindCityRepository {

    suspend fun findCity(query: String): FoundCity

    suspend fun saveCity(foundCity: FoundCity)

    class Base @Inject constructor(
        private val findCityCloudDataSource: FindCityCloudDataSource,
        private val findCityCacheDataSource: FindCityCacheDataSource,
    ) : FindCityRepository {

        override suspend fun findCity(query: String): FoundCity {
            val foundCityCloud = findCityCloudDataSource.findCity(query = query)
            return FoundCity(
                name = foundCityCloud.name,
                latitude = foundCityCloud.latitude,
                longitude = foundCityCloud.longitude,
            )
        }

        override suspend fun saveCity(foundCity: FoundCity) {
            findCityCacheDataSource.save(
                name = foundCity.name,
                latitude = foundCity.latitude,
                longitude = foundCity.longitude,
            )
        }

    }

}