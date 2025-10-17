package com.alexeyyuditsky.weatherapp.weather.domain

interface WeatherResult {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {

        fun mapToSuccess(weatherInCity: WeatherInCity): T

        fun mapToEmpty(): T

        fun mapToNoDataYet(): T
    }

    data class Success(
        private val weatherInCity: WeatherInCity,
    ) : WeatherResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapToSuccess(weatherInCity = weatherInCity)
    }

    data object NoDataYet : WeatherResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapToNoDataYet()
    }
}