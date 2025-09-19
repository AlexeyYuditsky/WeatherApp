package com.alexeyyuditsky.weatherapp.findCity.data

import javax.inject.Inject

interface FindCityCloudDataSource {

    suspend fun findCity(query: String): FoundCityCloud

    class Base @Inject constructor(
        private val service: FindCityService
    ) : FindCityCloudDataSource {

        override suspend fun findCity(query: String): FoundCityCloud {
            val result = service.findCity(query = query)
            return if (result.isEmpty())
                FoundCityCloud(
                    name = "",
                    latitude = 0f,
                    longitude = 0f,
                )
            else
                result[0]
        }

    }

}