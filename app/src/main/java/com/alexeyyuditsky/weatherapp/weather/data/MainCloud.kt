package com.alexeyyuditsky.weatherapp.weather.data

import com.google.gson.annotations.SerializedName

data class MainCloud(
    @SerializedName("temp") val temperature: Float
)