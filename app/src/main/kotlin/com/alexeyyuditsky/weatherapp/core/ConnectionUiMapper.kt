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
                    Connection.Status.CONNECTED -> {
                        ConnectionUi.Connected
                    }

                    Connection.Status.DISCONNECTED -> {
                        ConnectionUi.Disconnected
                    }

                    Connection.Status.CONNECTED_AFTER_DISCONNECTED -> {
                        ConnectionUi.ConnectedAfterDisconnected
                    }
                }
            }.stateIn(
                scope = applicationScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
                initialValue = if (connection.initialStatus == Connection.Status.CONNECTED) ConnectionUi.Connected else ConnectionUi.Disconnected
            )
    }
}