package com.alexeyyuditsky.weatherapp.weather.di

import com.alexeyyuditsky.weatherapp.weather.data.WeatherRepositoryImpl
import com.alexeyyuditsky.weatherapp.weather.data.FetchWeatherRepository
import com.alexeyyuditsky.weatherapp.weather.data.StartForegroundWrapper
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCacheDataSource
import com.alexeyyuditsky.weatherapp.weather.data.WeatherCloudDataSource
import com.alexeyyuditsky.weatherapp.weather.domain.CachedWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.FetchWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.LoadWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherRepository
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import com.alexeyyuditsky.weatherapp.weather.presentation.ErrorWeatherUiMapper
import com.alexeyyuditsky.weatherapp.weather.presentation.TimeWrapper
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUi
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUiMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface WeatherBindsModule {

    @Binds
    fun bindWeatherCloudDataSource(
        dataSource: WeatherCloudDataSource.Base,
    ): WeatherCloudDataSource

    @Binds
    fun bindWeatherCacheDataSource(
        dataSource: WeatherCacheDataSource.Base,
    ): WeatherCacheDataSource

    @Binds
    fun bindWeatherRepository(
        repository: WeatherRepositoryImpl,
    ): WeatherRepository

    @Binds
    fun bindsFetchWeatherRepository(
        repository: FetchWeatherRepository.Base,
    ): FetchWeatherRepository

    @Binds
    fun bindWeatherUiMapper(
        mapper: WeatherUiMapper,
    ): WeatherResult.Mapper<WeatherUi>

    @Binds
    fun bindErrorWeatherUiMapper(
        mapper: ErrorWeatherUiMapper.Base,
    ): ErrorWeatherUiMapper

    @Binds
    fun bindTimeWrapper(
        wrapper: TimeWrapper.Base,
    ): TimeWrapper

    @Binds
    fun bindForegroundWrapper(
        wrapper: StartForegroundWrapper.Base,
    ): StartForegroundWrapper

    @Binds
    fun bindLoadWeatherUseCase(
        useCase: LoadWeatherUseCase.Base,
    ): LoadWeatherUseCase

    @Binds
    fun bindFetchWeatherUseCase(
        useCase: FetchWeatherUseCase.Base,
    ): FetchWeatherUseCase

    @Binds
    fun bindCachedWeatherUseCase(
        useCase: CachedWeatherUseCase.Base,
    ): CachedWeatherUseCase
}