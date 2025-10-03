package com.alexeyyuditsky.weatherapp.weather.di

import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCloudDataSource
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUi
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUiMapper
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

    @Binds
    abstract fun bindWeatherUiMapper(mapper: WeatherUiMapper): WeatherResult.Mapper<WeatherUi>
}