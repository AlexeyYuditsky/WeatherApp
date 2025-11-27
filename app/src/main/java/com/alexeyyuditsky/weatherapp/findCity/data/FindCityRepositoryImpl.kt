package com.alexeyyuditsky.weatherapp.findCity.data

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import javax.inject.Inject

class FindCityRepositoryImpl @Inject constructor(
    private val cloudDataSource: FindCityCloudDataSource,
    private val cacheDataSource: FindCityCacheDataSource,
) : FindCityRepository {

    override suspend fun findCity(
        query: String,
    ): FoundCityResult = try {
        val foundCityCloudList = cloudDataSource.findCity(query = query)
        if (foundCityCloudList.isEmpty())
            FoundCityResult.Empty
        else {
            val foundCityCloud = foundCityCloudList.first()
            FoundCityResult.Success(
                foundCity = FoundCity(
                    name = foundCityCloud.name,
                    latitude = foundCityCloud.latitude,
                    longitude = foundCityCloud.longitude,
                )
            )
        }
    } catch (e: DomainException) {
        FoundCityResult.Error(error = e)
    }

    override suspend fun saveFoundCity(
        latitude: Float,
        longitude: Float,
    ) = cacheDataSource.save(
        latitude = latitude,
        longitude = longitude,
    )
}