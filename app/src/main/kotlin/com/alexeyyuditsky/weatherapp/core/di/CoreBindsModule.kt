package com.alexeyyuditsky.weatherapp.core.di

import com.alexeyyuditsky.weatherapp.core.Connection
import com.alexeyyuditsky.weatherapp.core.ConnectionUiMapper
import com.alexeyyuditsky.weatherapp.core.RunAsync
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoreBindsModule {

    @Binds
    @Singleton
    fun bindRunAsync(
        runAsync: RunAsync.Base
    ): RunAsync

    @Binds
    @Singleton
    fun bindConnection(
        connection: Connection.Base
    ): Connection

    @Binds
    @Singleton
    fun bindConnectionUiMapper(
        connection: ConnectionUiMapper.Base
    ): ConnectionUiMapper
}