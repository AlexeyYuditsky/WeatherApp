package com.alexeyyuditsky.weatherapp.findCity.di

import com.alexeyyuditsky.weatherapp.findCity.data.FindCityService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ViewModelComponent::class)
class FoundCityProvidesModule {

    @Provides
    fun provideService(retrofit: Retrofit): FindCityService =
        retrofit.create()

}