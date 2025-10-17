package com.alexeyyuditsky.weatherapp.weather.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherParams(
    val lat: Float,
    val lon: Float,
    val city: String,
    val time: Long,
    val imageUrl: String,
    val details: String,
    val forecast: List<Pair<String, String>> = emptyList(),
) : Parcelable {

    fun isEmpty() = time == 0L

    fun same(latitude: Float, longitude: Float) = lat == latitude && longitude == lon
}