package com.alexeyyuditsky.weatherapp.findCity.domain

interface FoundCityResult {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {

        fun mapBase(fountCity: FoundCity): T

        fun mapEmpty(): T

        fun mapNoConnectionError(): T
    }

    data class Base(
        private val foundCity: FoundCity,
    ) : FoundCityResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapBase(fountCity = foundCity)
    }

    data object Empty : FoundCityResult {

        override fun <T> map(mapper: Mapper<T>): T =
            mapper.mapEmpty()
    }


    data class Error(
        private val error: DomainException,
    ) : FoundCityResult {
        override fun <T> map(mapper: Mapper<T>): T =
            if (error is NoInternetException)
                mapper.mapNoConnectionError()
            else
                TODO("later")
    }

}

// 1) я создаю в юай слое объект который создаёт юай сущности
// 2) в домаин слое я создаю сущности которые возвращаются из репозитория
// 3) домаин сущности реализуют методы мап, которые принимают в себя сущность юай слоя
// 4) в домаин объекте я вызываю мап метод объекта юай слоя который соответствует тому какой юай объект я хочу вернуть из домаин объекта