package com.alexeyyuditsky.weatherapp.core.di

import com.alexeyyuditsky.weatherapp.core.presentation.Connection
import com.alexeyyuditsky.weatherapp.core.presentation.RunAsync
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreBindsModule {

    @Binds
    @Singleton
    abstract fun bindRunAsync(runAsync: RunAsync.Base): RunAsync

    @Binds
    @Singleton
    abstract fun bindsConnection(connection: Connection.Base): Connection
}