package com.alexeyyuditsky.weatherapp.findCity.domain

interface FoundCityResult {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {

        fun mapToSuccess(foundCity: FoundCity): T

        fun mapToEmpty(): T

        fun mapToNoConnectionError(): T

        fun mapToServiceUnavailableError(): T
    }

    data class Success(
        private val foundCity: FoundCity,
    ) : FoundCityResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapToSuccess(foundCity = foundCity)
    }

    data object Empty : FoundCityResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapToEmpty()
    }


    data class Error(
        private val error: DomainException,
    ) : FoundCityResult {

        override fun <T> map(mapper: Mapper<T>): T =
            if (error is NoInternetException)
                mapper.mapToNoConnectionError()
            else
                mapper.mapToServiceUnavailableError()
    }
}