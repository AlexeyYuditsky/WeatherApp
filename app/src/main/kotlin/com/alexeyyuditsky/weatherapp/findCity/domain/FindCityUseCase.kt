package com.alexeyyuditsky.weatherapp.findCity.domain

import javax.inject.Inject

interface FindCityUseCase {

    suspend fun invoke(query: String): FoundCityResult

    class Base @Inject constructor(
        private val repository: FindCityRepository,
    ) : FindCityUseCase {

        override suspend fun invoke(query: String) =
            repository.findCity(query = query)
    }
}