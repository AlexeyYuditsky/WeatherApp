package com.alexeyyuditsky.weatherapp.findCity.data

import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import com.alexeyyuditsky.weatherapp.findCity.domain.ServiceUnavailableException
import java.io.IOException
import javax.inject.Inject

interface FindCityCloudDataSource {

    suspend fun findCity(query: String): List<FoundCityCloud>

    class Base @Inject constructor(
        private val findCityService: FindCityService,
    ) : FindCityCloudDataSource {

        override suspend fun findCity(query: String): List<FoundCityCloud> =
            try {
                findCityService.findCity(query = query)
            } catch (e: Exception) {
                if (e is IOException)
                    throw NoInternetException
                else
                    throw ServiceUnavailableException
            }
    }
}