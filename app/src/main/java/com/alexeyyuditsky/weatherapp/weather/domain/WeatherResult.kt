package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException

interface WeatherResult {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {

        fun mapToSuccess(weatherInCity: WeatherInCity): T

        fun mapToLoading(): T

        fun mapToEmpty(): T

        fun mapToNoConnectionError(): T
    }

    data class Success(
        private val weatherInCity: WeatherInCity,
    ) : WeatherResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapToSuccess(weatherInCity = weatherInCity)
    }

    data class Error(
        private val error: DomainException,
    ) : WeatherResult {

        override fun <T> map(mapper: Mapper<T>): T =
            if (error is NoInternetException)
                mapper.mapToNoConnectionError()
            else
                TODO("later")
    }
}