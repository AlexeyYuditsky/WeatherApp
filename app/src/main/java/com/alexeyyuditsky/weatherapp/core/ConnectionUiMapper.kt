package com.alexeyyuditsky.weatherapp.core

import com.alexeyyuditsky.weatherapp.core.di.ApplicationScope
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectionUiMapper {

    val state: SharedFlow<ConnectionUi>

    @Singleton
    class Base @Inject constructor(
        @ApplicationScope applicationScope: CoroutineScope,
        connection: Connection,
    ) : ConnectionUiMapper {

        override val state: SharedFlow<ConnectionUi> = connection.state
            .map {
                when (it) {
                    Connection.Status.Connected ->
                        ConnectionUi.Connected

                    Connection.Status.Disconnected ->
                        ConnectionUi.Disconnected
                }
            }.shareIn(
                scope = applicationScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
    }
}