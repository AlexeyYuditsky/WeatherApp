package com.alexeyyuditsky.weatherapp.findCity.di

import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCacheDataSource
import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCloudDataSource
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUi
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUiMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class FoundCityBindsModule {

    @Binds
    abstract fun bindFindCityCloudDataSource(cloudDataSource: FindCityCloudDataSource.Base): FindCityCloudDataSource

    @Binds
    abstract fun bindFindCityCacheDataSource(cacheDataSource: FindCityCacheDataSource.Base): FindCityCacheDataSource

    @Binds
    abstract fun bindFindCityRepository(repository: FindCityRepository.Base): FindCityRepository

    @Binds
    abstract fun bindFoundCityUiMapper(mapper: FoundCityUiMapper): FoundCityResult.Mapper<FoundCityUi>
}