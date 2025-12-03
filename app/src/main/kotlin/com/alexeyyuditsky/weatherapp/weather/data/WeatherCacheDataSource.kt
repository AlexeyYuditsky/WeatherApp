package com.alexeyyuditsky.weatherapp.weather.data

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherCacheDataSource {

    suspend fun saveWeather(params: WeatherParams)

    suspend fun saveHasError(hasError: Boolean)

    val cityParams: Pair<Float, Float>

    val cachedWeatherFlow: Flow<WeatherParams>

    val widgetWeatherFlow: Flow<String>

    val hasErrorFlow: Flow<Boolean>

    @Singleton
    class Base @Inject constructor(
        private val dataStore: DataStore<Preferences>,
        sharedPreferences: SharedPreferences,
    ) : WeatherCacheDataSource {

        private val gson = Gson()
        private val weatherWidget = stringPreferencesKey("weather_widget")
        private val weatherParams = stringPreferencesKey("weather_params")
        private val hasErrorKey = booleanPreferencesKey("weather_error")

        private val dateFormat = SimpleDateFormat("HH:mm\ndd-MMM", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }

        override suspend fun saveHasError(hasError: Boolean) {
            dataStore.edit { prefs ->
                prefs[hasErrorKey] = hasError
            }
        }

        override suspend fun saveWeather(params: WeatherParams) {
            val timeUi = dateFormat.format(Date(params.time))
            val temperature = params.details
                .substringAfter("Temperature: ")
                .substringBefore("°C") + "°C"

            dataStore.edit { prefs ->
                prefs[weatherParams] = gson.toJson(params)
                prefs[weatherWidget] = "${params.imageUrl};$temperature;$timeUi"
            }
        }

        override val cityParams: Pair<Float, Float> =
            Pair(
                sharedPreferences.getFloat(LATITUDE, 0f),
                sharedPreferences.getFloat(LONGITUDE, 0f)
            )

        override val cachedWeatherFlow: Flow<WeatherParams> =
            dataStore.data.map { preferences ->
                val raw = preferences[weatherParams] ?: gson.toJson(
                    WeatherParams(
                        latitude = 0f,
                        longitude = 0f,
                        city = "",
                        time = 0L,
                        imageUrl = "",
                        details = ""
                    )
                )
                gson.fromJson(raw, WeatherParams::class.java)
            }

        override val widgetWeatherFlow: Flow<String> =
            dataStore.data.map { preferences ->
                preferences[weatherWidget] ?: ""
            }

        override val hasErrorFlow: Flow<Boolean> =
            dataStore.data.map { preferences ->
                preferences[hasErrorKey] ?: false
            }

        private companion object {
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
        }
    }
}