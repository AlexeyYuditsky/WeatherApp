package com.alexeyyuditsky.weatherapp.core

import kotlinx.serialization.Serializable

interface Screen {

    @Serializable
    data object FindCity : Screen

    @Serializable
    data object Weather : Screen
}