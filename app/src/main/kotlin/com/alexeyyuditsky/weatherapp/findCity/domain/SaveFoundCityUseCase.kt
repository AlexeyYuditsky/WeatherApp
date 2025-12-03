package com.alexeyyuditsky.weatherapp.findCity.domain

import javax.inject.Inject

interface SaveFoundCityUseCase {

    suspend operator fun invoke(
        latitude: Float,
        longitude: Float,
    )

    class Base @Inject constructor(
        private val repository: FindCityRepository,
    ) : SaveFoundCityUseCase {

        override suspend operator fun invoke(
            latitude: Float,
            longitude: Float,
        ) = repository.saveFoundCity(
            latitude = latitude,
            longitude = longitude,
        )
    }
}