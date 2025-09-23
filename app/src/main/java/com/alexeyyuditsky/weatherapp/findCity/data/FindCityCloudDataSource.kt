package com.alexeyyuditsky.weatherapp.findCity.data

import javax.inject.Inject

interface FindCityCloudDataSource {

    suspend fun findCity(query: String): FoundCityCloud

    class Base @Inject constructor(
        private val findCityService: FindCityService,
    ) : FindCityCloudDataSource {

        override suspend fun findCity(query: String): FoundCityCloud {
            val result = findCityService.findCity(query = query)
            return if (result.isEmpty())
                FoundCityCloud(
                    name = "",
                    latitude = 0f,
                    longitude = 0f,
                )
            else
                result.first()
        }

    }

}