package com.alexeyyuditsky.weatherapp.weather.di

import com.alexeyyuditsky.weatherapp.weather.data.StartForegroundWrapper
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCloudDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherRepositoryImpl
import com.alexeyyuditsky.weatherapp.weather.domain.RefreshWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.SaveWeatherExceptionUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WeatherSingletonBindsModule {

    @Binds
    @Singleton
    fun bindWeatherCloudDataSource(
        dataSource: WeatherCloudDataSource.Base,
    ): WeatherCloudDataSource

    @Binds
    @Singleton
    fun bindWeatherCacheDataSource(
        dataSource: WeatherCacheDataSource.Base,
    ): WeatherCacheDataSource

    @Binds
    @Singleton
    fun bindWeatherRepository(
        repository: WeatherRepositoryImpl,
    ): WeatherRepository

    @Binds
    @Singleton
    fun bindForegroundWrapper(
        wrapper: StartForegroundWrapper.Base,
    ): StartForegroundWrapper

    @Binds
    @Singleton
    fun bindRefreshWeatherUseCase(
        useCase: RefreshWeatherUseCase.Base,
    ): RefreshWeatherUseCase

    @Binds
    @Singleton
    fun bindSaveWeatherExceptionUseCase(
        useCase: SaveWeatherExceptionUseCase.Base,
    ): SaveWeatherExceptionUseCase
}