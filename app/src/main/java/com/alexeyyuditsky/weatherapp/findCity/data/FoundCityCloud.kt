package com.alexeyyuditsky.weatherapp.findCity.data

import com.google.gson.annotations.SerializedName

data class FoundCityCloud(
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "lat")
    val latitude: Float,
    @SerializedName(value = "lon")
    val longitude: Float,
)