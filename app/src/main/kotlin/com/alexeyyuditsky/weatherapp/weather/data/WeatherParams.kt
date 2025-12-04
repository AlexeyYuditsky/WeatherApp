package com.alexeyyuditsky.weatherapp.weather.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherParams(
    val latitude: Float = 0f,
    val longitude: Float = 0f,
    val city: String = "",
    val time: Long = 0L,
    val imageUrl: String = "",
    val details: String = "",
    val forecast: List<Pair<String, String>> = emptyList(),
) : Parcelable {

    fun isEmpty() = time == 0L

    fun same(latitude: Float, longitude: Float) =
        this@WeatherParams.latitude == latitude && longitude == this@WeatherParams.longitude
}