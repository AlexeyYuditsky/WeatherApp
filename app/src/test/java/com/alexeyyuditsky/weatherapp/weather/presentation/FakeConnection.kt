package com.alexeyyuditsky.weatherapp.weather.presentation

import com.alexeyyuditsky.weatherapp.core.presentation.Connection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeConnection : Connection {

    private val flow = MutableStateFlow(true)
    override val connected get() = flow.asStateFlow()

    fun change(connected: Boolean) {
        flow.value = connected
    }
}