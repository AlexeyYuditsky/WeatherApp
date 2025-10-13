package com.alexeyyuditsky.weatherapp.weather.data

import android.content.Context
import com.alexeyyuditsky.weatherapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface WeatherCacheDataSource {

    fun cityParams(): Pair<Float, Float>

    class Base @Inject constructor(
        @ApplicationContext context: Context
    ) : WeatherCacheDataSource {

        private val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )

        override fun cityParams(): Pair<Float, Float> {
            val latitude = sharedPreferences.getFloat(LATITUDE, 0f)
            val longitude = sharedPreferences.getFloat(LONGITUDE, 0f)
            return latitude to longitude
        }

        private companion object {
            const val LATITUDE = "latitudeKey"
            const val LONGITUDE = "longitudeKey"
        }
    }
}