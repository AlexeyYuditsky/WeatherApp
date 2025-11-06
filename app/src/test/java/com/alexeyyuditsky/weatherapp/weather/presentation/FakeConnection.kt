package com.alexeyyuditsky.weatherapp.weather.presentation

import com.alexeyyuditsky.weatherapp.core.Connection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeConnection : Connection {

    private val flow = MutableStateFlow(true)
    override val state get() = flow.asStateFlow()

    fun change(connected: Boolean) {
        flow.value = connected
    }
}