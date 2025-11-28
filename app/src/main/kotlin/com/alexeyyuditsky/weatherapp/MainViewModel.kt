package com.alexeyyuditsky.weatherapp

import androidx.lifecycle.ViewModel
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherCacheDataSource: WeatherCacheDataSource
) : ViewModel() {

    fun hasAlreadyChosenLocation(): Boolean {
        val (latitude, longitude) = weatherCacheDataSource.cityParams()
        return latitude != 0f && longitude != 0f
    }
}