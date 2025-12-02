package com.alexeyyuditsky.weatherapp.core.di

import com.alexeyyuditsky.weatherapp.core.Connection
import com.alexeyyuditsky.weatherapp.core.ConnectionUiMapper
import com.alexeyyuditsky.weatherapp.core.RunAsync
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoreBindsModule {

    @Binds
    fun bindRunAsync(
        runAsync: RunAsync.Base
    ): RunAsync

    @Binds
    fun bindConnection(
        connection: Connection.Base
    ): Connection

    @Binds
    fun bindConnectionUiMapper(
        connection: ConnectionUiMapper.Base
    ): ConnectionUiMapper
}