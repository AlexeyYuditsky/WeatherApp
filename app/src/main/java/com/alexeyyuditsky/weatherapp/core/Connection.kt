package com.alexeyyuditsky.weatherapp.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
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

        override val state: Flow<Status> = callbackFlow {

            fun getCurrentNetworkStatus(networkCapabilities: NetworkCapabilities): Status =
                if (
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                )
                    Status.Connected.also { log("Connection: getCurrentNetworkStatus: Status.Connected") }
                else
                    Status.Disconnected.also { log("Connection: getCurrentNetworkStatus: Status.Disconnected") }

            fun getConnectionStatus(): Status =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
                    getCurrentNetworkStatus(it)
                }
                    ?: Status.Disconnected.also { log("Connection: getConnectionStatus: Status.Disconnected") }

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(getConnectionStatus()).also { log("Connection: onAvailable") }
                }

                override fun onLost(network: Network) {
                    trySend(getConnectionStatus()).also { log("Connection: onLost") }
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities,
                ) {
                    trySend(getCurrentNetworkStatus(networkCapabilities).also { log("Connection: onCapabilitiesChanged") })
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            trySend(getConnectionStatus())
            awaitClose { connectivityManager.unregisterNetworkCallback(callback).also { log("Connection: awaitClose: unregisterNetworkCallback") } }
        }
            .conflate()
            .distinctUntilChanged()
    }
}