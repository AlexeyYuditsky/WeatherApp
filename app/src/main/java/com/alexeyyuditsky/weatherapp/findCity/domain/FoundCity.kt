package com.alexeyyuditsky.weatherapp.findCity.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoundCity(
    val name: String,
    val latitude: Float,
    val longitude: Float
) : Parcelable