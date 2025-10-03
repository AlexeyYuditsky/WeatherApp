package com.alexeyyuditsky.weatherapp.weather.domain

import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException

interface WeatherResult {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {

        fun mapBase(weatherInCity: WeatherInCity): T

        fun mapEmpty(): T

        fun mapNoConnectionError(): T
    }

    data class Base(
        private val weatherInCity: WeatherInCity,
    ) : WeatherResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapBase(weatherInCity = weatherInCity)
    }

    data class Error(
        private val error: DomainException,
    ) : WeatherResult {

        override fun <T> map(mapper: Mapper<T>): T =
            if (error is NoInternetException)
                mapper.mapNoConnectionError()
            else
                TODO("later")
    }
}