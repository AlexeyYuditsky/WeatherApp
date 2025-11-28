package com.alexeyyuditsky.weatherapp.findCity.data

import com.alexeyyuditsky.weatherapp.core.data.CloudDataSource
import javax.inject.Inject

interface FindCityCloudDataSource : CloudDataSource {

    suspend fun findCity(
        query: String,
    ): List<FoundCityCloud>

    class Base @Inject constructor(
        private val findCityService: FindCityService,
    ) : FindCityCloudDataSource {

        override suspend fun findCity(
            query: String,
        ): List<FoundCityCloud> = handle {
            findCityService.findCity(query = query)
        }
    }
}