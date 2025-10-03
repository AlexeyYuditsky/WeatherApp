package com.alexeyyuditsky.weatherapp.weather.di

import com.alexeyyuditsky.weatherapp.weather.data.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ViewModelComponent::class)
class WeatherProvidesModule {

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService =
        retrofit.create()
}