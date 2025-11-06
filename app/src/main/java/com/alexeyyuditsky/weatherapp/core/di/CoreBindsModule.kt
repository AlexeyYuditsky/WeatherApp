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
abstract class CoreBindsModule {

    @Binds
    abstract fun bindRunAsync(runAsync: RunAsync.Base): RunAsync

    @Binds
    abstract fun bindsConnection(connection: Connection.Base): Connection

    @Binds
    abstract fun bindsConnectionUiMapper(connection: ConnectionUiMapper.Base): ConnectionUiMapper
}