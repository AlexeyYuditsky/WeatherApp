package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import javax.inject.Inject
import javax.inject.Singleton

interface SaveWeatherExceptionUseCase {

    suspend operator fun invoke(exception: DomainException)

    @Singleton
    class Base @Inject constructor(
        private val repository: WeatherRepository,
    ) : SaveWeatherExceptionUseCase {

        override suspend operator fun invoke(exception: DomainException) =
            repository.saveException(exception = exception)
    }
}