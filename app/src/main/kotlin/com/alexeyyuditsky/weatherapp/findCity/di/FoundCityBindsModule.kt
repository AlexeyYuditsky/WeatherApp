package com.alexeyyuditsky.weatherapp.findCity.di

import com.alexeyyuditsky.weatherapp.findCity.data.FindCityRepositoryImpl
import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCacheDataSource
import com.alexeyyuditsky.weatherapp.findCity.data.FindCityCloudDataSource
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityUseCase
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import com.alexeyyuditsky.weatherapp.findCity.domain.SaveFoundCityUseCase
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUi
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUiMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface FoundCityBindsModule {

    @Binds
    fun bindFindCityCloudDataSource(
        dataSource: FindCityCloudDataSource.Base,
    ): FindCityCloudDataSource

    @Binds
    fun bindFindCityCacheDataSource(
        dataSource: FindCityCacheDataSource.Base,
    ): FindCityCacheDataSource

    @Binds
    fun bindFindCityRepository(
        repository: FindCityRepositoryImpl,
    ): FindCityRepository

    @Binds
    fun bindFoundCityUiMapper(
        mapper: FoundCityUiMapper,
    ): FoundCityResult.Mapper<FoundCityUi>

    @Binds
    fun bindFindCityUseCase(
        useCase: FindCityUseCase.Base,
    ): FindCityUseCase

    @Binds
    fun bindSaveFoundCityUseCase(
        useCase: SaveFoundCityUseCase.Base,
    ): SaveFoundCityUseCase
}