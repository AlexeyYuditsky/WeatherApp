package com.alexeyyuditsky.weatherapp.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

interface Connection {

    val state: Flow<Status>

    enum class Status { Connected, Disconnected }

    @Singleton
    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : Connection {

        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @OptIn(FlowPreview::class)
        override val state: Flow<Status> = callbackFlow {

            fun getConnectionStatus(): Status =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
                    if (
                        it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    )
                        Status.Connected
                    else
                        Status.Disconnected
                } ?: Status.Disconnected

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(Status.Connected).also { log("Connection: onAvailable: Status.Connected") }
                }

                override fun onLost(network: Network) {
                    trySend(Status.Disconnected).also { log("Connection: onAvailable: Status.Disconnected") }
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