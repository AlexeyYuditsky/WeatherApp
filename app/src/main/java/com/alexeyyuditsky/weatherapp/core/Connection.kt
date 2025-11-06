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

            fun currentConnection(): Status =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    if (
                        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    )
                        Status.Connected
                    else
                        Status.Disconnected
                } ?: Status.Disconnected

            trySend(currentConnection())

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(Status.Connected)
                }

                override fun onLost(network: Network) {
                    trySend(Status.Disconnected)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    caps: NetworkCapabilities,
                ) {
                    trySend(
                        if (
                            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                        )
                            Status.Connected
                        else
                            Status.Disconnected
                    )
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }
            .distinctUntilChanged()
            .conflate()

    }
}