package com.alexeyyuditsky.weatherapp.weather.data

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alexeyyuditsky.weatherapp.R
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface WeatherCacheDataSource {

    fun cityParams(): Pair<Float, Float>

    suspend fun saveWeather(params: WeatherParams)

    fun savedWeather(): Flow<WeatherParams>

    fun weatherForWidget(): Flow<String>

    suspend fun saveHasError(hasError: Boolean)

    fun hasError(): Flow<Boolean>

    class Base @Inject constructor(
        @ApplicationContext private val context: Context,
    ) : WeatherCacheDataSource {

        private val gson = Gson()

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = context.getString(R.string.app_name)
        )

        private val sharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

        override fun cityParams(): Pair<Float, Float> {
            val latitude = sharedPreferences.getFloat(LATITUDE, 0f)
            val longitude = sharedPreferences.getFloat(LONGITUDE, 0f)
            return Pair(latitude, longitude)
        }

        private val dateFormat = SimpleDateFormat("HH:mm\ndd-MMM", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }

        override suspend fun saveWeather(params: WeatherParams) {
            val timeUi = dateFormat.format(Date(params.time))
            val temperature =
                params.details.substringAfter("Temperature: ").substringBefore("°C") + "°C"

            context.dataStore.edit { prefs ->
                prefs[weatherParams] = gson.toJson(params)
                prefs[weatherWidget] = "${params.imageUrl};$temperature;$timeUi"
            }
        }

        override fun weatherForWidget(): Flow<String> {
            return context.dataStore.data.map { preferences ->
                preferences[weatherWidget] ?: ""
            }
        }

        override fun savedWeather(): Flow<WeatherParams> =
            context.dataStore.data.map { preferences ->
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

        override suspend fun saveHasError(hasError: Boolean) {
            context.dataStore.edit { prefs ->
                prefs[hasErrorKey] = hasError
            }
        }

        override fun hasError() = context.dataStore.data.map { preferences ->
            preferences[hasErrorKey] ?: false
        }

        private val weatherWidget = stringPreferencesKey("weather_widget")
        private val weatherParams = stringPreferencesKey("weather_params")
        private val hasErrorKey = booleanPreferencesKey("weather_error")

        private companion object {
            const val LATITUDE = "latitudeKey"
            const val LONGITUDE = "longitudeKey"
        }
    }
}