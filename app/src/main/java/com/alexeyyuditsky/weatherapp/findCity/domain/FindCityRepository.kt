package com.alexeyyuditsky.weatherapp.findCity.domain

import kotlin.String

interface FindCityRepository {

    suspend fun findCity(
        query: String,
    ): FoundCityResult

    suspend fun saveFoundCity(
        foundCity: FoundCity,
    )

    suspend fun saveFoundCity(
        latitude: Double,
        longitude: Double,
    )
}