package com.alexeyyuditsky.weatherapp.weather.presentation

class FakeTimeWrapper : TimeWrapper {

    override fun getHumanReadableTime(timeMillis: Long): String {
        return timeMillis.toString()
    }
}