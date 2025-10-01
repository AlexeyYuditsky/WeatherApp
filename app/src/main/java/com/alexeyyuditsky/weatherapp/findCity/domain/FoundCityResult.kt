package com.alexeyyuditsky.weatherapp.findCity.domain

import android.os.Parcelable

interface FoundCityResult {

    fun <T : Parcelable> map(mapper: Mapper<T>): T

    interface Mapper<T : Parcelable> {

        fun mapFoundCity(fountCity: FoundCity): T

        fun mapEmpty(): T

        fun mapNoConnection(): T
    }

    data class Base(
        private val foundCity: FoundCity,
    ) : FoundCityResult {

        override fun <T : Parcelable> map(mapper: Mapper<T>): T =
            mapper.mapFoundCity(fountCity = foundCity)
    }

    data class Failed(
        private val error: DomainException,
    ) : FoundCityResult {

        override fun <T : Parcelable> map(mapper: Mapper<T>): T =
            if (error is NoInternetException)
                mapper.mapNoConnection()
            else
                TODO("later")
    }

    data object Empty : FoundCityResult {

        override fun <T : Parcelable> map(mapper: Mapper<T>): T =
            mapper.mapEmpty()
    }

}