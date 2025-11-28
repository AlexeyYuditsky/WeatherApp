package com.alexeyyuditsky.weatherapp.weather.data

import com.google.gson.annotations.SerializedName

data class AirPollutionCloud(
    @SerializedName("list") val list: List<AirPollutionCloudInner>
)

data class AirPollutionCloudInner(
    @SerializedName("main") val main: AirPollutionMainCloud
)

data class AirPollutionMainCloud(
    @SerializedName("aqi") val aqi: Int
) {

    fun ui(): String = when (aqi) {
        1 -> PREFIX + "good"
        2 -> PREFIX + "fair"
        3 -> PREFIX + "moderate"
        4 -> PREFIX + "poor"
        5 -> PREFIX + "very poor"
        else -> ""
    }

    companion object {
        private const val PREFIX = "\nAir pollution: "
    }
}