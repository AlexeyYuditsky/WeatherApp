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

    fun cityParams(): Pair<Float, Float>

    suspend fun saveWeather(params: WeatherParams)

    fun savedWeather(): Flow<WeatherParams>

    fun weatherForWidget(): Flow<String>

    suspend fun saveHasError(hasError: Boolean)

    fun hasError(): Flow<Boolean>

    @Singleton
    class Base @Inject constructor(
        private val dataStore: DataStore<Preferences>,
        private val sharedPreferences: SharedPreferences,
    ) : WeatherCacheDataSource {

        private val gson = Gson()
        private val weatherWidget = stringPreferencesKey("weather_widget")
        private val weatherParams = stringPreferencesKey("weather_params")
        private val hasErrorKey = booleanPreferencesKey("weather_error")

        private val default = WeatherParams(
            latitude = 0f,
            longitude = 0f,
            city = "",
            time = 0L,
            imageUrl = "",
            details = ""
        )

        private val dateFormat = SimpleDateFormat("HH:mm\ndd-MMM", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }

        override fun cityParams(): Pair<Float, Float> {
            val latitude = sharedPreferences.getFloat(LATITUDE, 0f)
            val longitude = sharedPreferences.getFloat(LONGITUDE, 0f)
            return Pair(latitude, longitude)
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

        override fun weatherForWidget(): Flow<String> =
            dataStore.data.map { preferences ->
                preferences[weatherWidget] ?: ""
            }

        override fun savedWeather() = dataStore.data.map { preferences ->
            val raw = preferences[weatherParams] ?: gson.toJson(default)
            gson.fromJson(raw, WeatherParams::class.java)
        }

        override suspend fun saveHasError(hasError: Boolean) {
            dataStore.edit { prefs ->
                prefs[hasErrorKey] = hasError
            }
        }

        override fun hasError() = dataStore.data.map { preferences ->
            preferences[hasErrorKey] ?: false
        }

        private companion object {
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
        }
    }
}