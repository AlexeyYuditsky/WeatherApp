package com.alexeyyuditsky.weatherapp.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

interface Connection {

    val state: Flow<Status>

    enum class Status { Connected, ConnectedAfterDisconnected, Disconnected }

    @Singleton
    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : Connection {

        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @OptIn(FlowPreview::class)
        override val state: Flow<Status> = callbackFlow {

            var previousStatus: Status? = null

            fun getConnectionStatus(): Status {
                val status =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                        ?.let {
                            if (
                                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                            )
                                Status.Connected
                            else
                                Status.Disconnected
                        } ?: Status.Disconnected

                previousStatus = status
                return status
            }

            fun determineAndSend() = launch {
                if (previousStatus == Status.Disconnected) {
                    send(Status.ConnectedAfterDisconnected).also { log("Connection: sendStatus: Status.ConnectedAfterDisconnected") }
                    delay(1000)
                    send(Status.Connected).also { log("Connection: sendStatus: Status.Connected") }
                } else {
                    send(Status.Connected).also { log("Connection: sendStatus: Status.Connected") }
                }
                previousStatus = null
            }

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    determineAndSend()
                }

                override fun onLost(network: Network) {
                    previousStatus = Status.Disconnected
                    trySend(Status.Disconnected).also { log("Connection: onLost: Status.Disconnected") }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            trySend(getConnectionStatus())
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }
            .debounce(300) // ignore rapid onAvailable/onLost events triggered during network switching
            .distinctUntilChanged()
    }
}