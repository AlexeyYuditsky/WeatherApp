package com.alexeyyuditsky.weatherapp.findCity.data

import android.content.Context
import androidx.core.content.edit
import com.alexeyyuditsky.weatherapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface FindCityCacheDataSource {

    suspend fun save(
        name: String,
        latitude: Float,
        longitude: Float,
    )

    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : FindCityCacheDataSource {

        private val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )

        override suspend fun save(
            name: String,
            latitude: Float,
            longitude: Float,
        ) = sharedPreferences.edit {
            putString(NAME, name)
            putFloat(LATITUDE, latitude)
            putFloat(LONGITUDE, longitude)
        }

        private companion object {
            const val NAME = "cityNameKey"
            const val LATITUDE = "latitudeKey"
            const val LONGITUDE = "longitudeKey"
        }

    }

}