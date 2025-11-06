package com.alexeyyuditsky.weatherapp.weather.di

import com.alexeyyuditsky.weatherapp.weather.data.WeatherRepositoryImpl
import com.alexeyyuditsky.weatherapp.weather.data.FetchWeatherRepository
import com.alexeyyuditsky.weatherapp.weather.data.StartForegroundWrapper
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCloudDataSource
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import com.alexeyyuditsky.weatherapp.weather.presentation.TimeWrapper
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUi
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUiMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherBindsModule {

    @Binds
    abstract fun bindWeatherCloudDataSource(
        cloudDataSource: WeatherCloudDataSource.Base,
    ): WeatherCloudDataSource

    @Binds
    abstract fun bindWeatherCacheDataSource(
        cacheDataSource: WeatherCacheDataSource.Base,
    ): WeatherCacheDataSource

    @Binds
    abstract fun bindWeatherRepository(
        repository: WeatherRepositoryImpl,
    ): WeatherRepository

    @Binds
    abstract fun bindsFetchWeatherRepository(
        fetchRepository: FetchWeatherRepository.Base,
    ): FetchWeatherRepository

    @Binds
    abstract fun bindWeatherUiMapper(
        mapper: WeatherUiMapper,
    ): WeatherResult.Mapper<WeatherUi>

    @Binds
    abstract fun bindTimeWrapper(
        wrapper: TimeWrapper.Base,
    ): TimeWrapper

    @Binds
    abstract fun bindForegroundWrapper(
        wrapper: StartForegroundWrapper.Base,
    ): StartForegroundWrapper
}