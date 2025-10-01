package com.alexeyyuditsky.weatherapp.findCity.domain

import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCacheDataSource
import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCloudDataSource
import javax.inject.Inject
import kotlin.String

interface FindCityRepository {

    suspend fun findCity(query: String): FoundCityResult

    suspend fun saveCity(foundCity: FoundCity)

    class Base @Inject constructor(
        private val cloudDataSource: FindCityCloudDataSource,
        private val cacheDataSource: FindCityCacheDataSource,
    ) : FindCityRepository {

        override suspend fun findCity(query: String): FoundCityResult = try {
            val foundCityCloudList = cloudDataSource.findCity(query = query)
            if (foundCityCloudList.isEmpty())
                FoundCityResult.Empty
            else {
                val foundCityCloud = foundCityCloudList.first()
                FoundCityResult.Base(
                    foundCity = FoundCity(
                        name = foundCityCloud.name,
                        latitude = foundCityCloud.latitude,
                        longitude = foundCityCloud.longitude,
                    )
                )
            }
        } catch (e: DomainException) {
            FoundCityResult.Failed(error = e)
        }

        override suspend fun saveCity(foundCity: FoundCity) =
            cacheDataSource.save(
                name = foundCity.name,
                latitude = foundCity.latitude,
                longitude = foundCity.longitude,
            )
    }

}