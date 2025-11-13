package com.alexeyyuditsky.weatherapp.core

import com.alexeyyuditsky.weatherapp.core.di.ApplicationScope
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectionUiMapper {

    val state: StateFlow<ConnectionUi>

    @Singleton
    class Base @Inject constructor(
        @ApplicationScope applicationScope: CoroutineScope,
        connection: Connection,
    ) : ConnectionUiMapper {

        override val state = connection.statuses
            .map {
                when (it) {
                    Connection.Status.Connected -> {
                        ConnectionUi.Connected.also { log("ConnectionUiMapper: map: ConnectionUi.Connected") }
                    }

                    Connection.Status.Disconnected -> {
                        ConnectionUi.Disconnected.also { log("ConnectionUiMapper: map: ConnectionUi.Disconnected") }
                    }

                    Connection.Status.ConnectedAfterDisconnected -> {
                        ConnectionUi.ConnectedAfterDisconnected.also { log("ConnectionUiMapper: map: ConnectionUi.ConnectedAfterDisconnected") }
                    }
                }
            }.stateIn(
                scope = applicationScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 1000),
                initialValue = if (connection.initialStatus == Connection.Status.Connected)
                    ConnectionUi.Connected
                else
                    ConnectionUi.Disconnected
            )
    }
}