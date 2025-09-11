package com.alexeyyuditsky.weatherapp.findCity.domain

interface FindCityRepository {

    suspend fun findCity(query: String): FoundCity

    suspend fun saveCity(foundCity: FoundCity)

}