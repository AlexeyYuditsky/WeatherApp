package com.alexeyyuditsky.weatherapp.weather.di

import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCloudDataSource
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class WeatherBindsModule {

    @Binds
    abstract fun bindWeatherCloudDataSource(cloudDataSource: WeatherCloudDataSource.Base): WeatherCloudDataSource

    @Binds
    abstract fun bindWeatherCacheDataSource(cacheDataSource: WeatherCacheDataSource.Base): WeatherCacheDataSource

    @Binds
    abstract fun bindWeatherRepository(repository: WeatherRepository.Base): WeatherRepository

}