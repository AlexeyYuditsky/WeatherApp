package com.alexeyyuditsky.weatherapp.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherCloud(
    @SerializedName("main") val main: MainCloud
)