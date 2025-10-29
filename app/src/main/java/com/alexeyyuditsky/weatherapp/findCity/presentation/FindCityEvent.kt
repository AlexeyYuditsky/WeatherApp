package com.alexeyyuditsky.weatherapp.findCity.presentation

sealed interface FindCityEvent {

    data object NavigateToWeatherScreen : FindCityEvent
}