package com.alexeyyuditsky.weatherapp.findCity.domain

abstract class DomainException : Exception()

data object NoInternetException : DomainException() {
    private fun readResolve(): Any = NoInternetException
}

data object ServiceUnavailableException : DomainException() {
    private fun readResolve(): Any = ServiceUnavailableException
}