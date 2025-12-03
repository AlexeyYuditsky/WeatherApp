package com.alexeyyuditsky.weatherapp.weather.di

import com.alexeyyuditsky.weatherapp.weather.domain.CachedWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.FetchWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.LoadWeatherUseCase
import com.alexeyyuditsky.weatherapp.weather.domain.WeatherResult
import com.alexeyyuditsky.weatherapp.weather.presentation.ErrorWeatherUiMapper
import com.alexeyyuditsky.weatherapp.weather.presentation.TimeWrapper
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUi
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUiMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface WeatherViewModelBindsModule {

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
}