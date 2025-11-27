package com.alexeyyuditsky.weatherapp.findCity.data

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

interface FindCityCacheDataSource {

    suspend fun save(
        latitude: Float,
        longitude: Float,
    )

    class Base @Inject constructor(
        private val sharedPreferences: SharedPreferences,
    ) : FindCityCacheDataSource {

        override suspend fun save(
            latitude: Float,
            longitude: Float,
        ) = sharedPreferences.edit {
            putFloat(LATITUDE, latitude)
            putFloat(LONGITUDE, longitude)
        }

        private companion object {
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
        }
    }
}