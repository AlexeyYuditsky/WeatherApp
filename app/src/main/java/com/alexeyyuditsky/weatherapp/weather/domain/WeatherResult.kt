package com.alexeyyuditsky.weatherapp.weather.domain

import android.os.Parcelable
import com.alexeyyuditsky.weatherapp.findCity.domain.DomainException
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException

interface WeatherResult {

    fun <T : Parcelable> map(mapper: Mapper<T>): T

    interface Mapper<T : Parcelable> {

        fun mapWeatherInCity(weatherInCity: WeatherInCity): T

        fun mapNoConnection(): T

        fun mapEmpty(): T
    }

    data class Base(
        private val weatherInCity: WeatherInCity,
    ) : WeatherResult {

        override fun <T : Parcelable> map(mapper: Mapper<T>): T =
            mapper.mapWeatherInCity(weatherInCity = weatherInCity)
    }

    data class Failed(
        private val error: DomainException,
    ) : WeatherResult {

        override fun <T : Parcelable> map(mapper: Mapper<T>): T =
            if (error is NoInternetException)
                mapper.mapNoConnection()
            else
                TODO("later")
    }

}