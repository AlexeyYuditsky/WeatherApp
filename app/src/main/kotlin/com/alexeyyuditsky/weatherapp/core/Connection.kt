package com.alexeyyuditsky.weatherapp.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject
import javax.inject.Singleton

interface Connection {

    val initialStatus: Status
    val statuses: Flow<Status>

    enum class Status { CONNECTED, CONNECTED_AFTER_DISCONNECTED, DISCONNECTED }

    @Singleton
    class Base @Inject constructor(@ApplicationContext context: Context) : Connection {

        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        private val isConnectedAtStart: Boolean = isConnectedAtStart()

        private val networkStatus: Flow<Boolean> = callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    trySend(false)
                }
            }

            trySend(isConnectedAtStart)
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }

        override val initialStatus: Status =
            if (isConnectedAtStart) Status.CONNECTED else Status.DISCONNECTED

        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        override val statuses: Flow<Status> = networkStatus
            .debounce(300)
            .distinctUntilChanged()
            .let { flow ->
                var wasDisconnected = !isConnectedAtStart

                flow.transformLatest { isConnected ->
                    if (isConnected) {
                        if (wasDisconnected) {
                            emit(Status.CONNECTED_AFTER_DISCONNECTED)
                            delay(1000)
                        }
                        emit(Status.CONNECTED)
                        wasDisconnected = false
                    } else {
                        delay(1000)
                        wasDisconnected = true
                        emit(Status.DISCONNECTED)
                    }
                }
            }

        private fun isConnectedAtStart(): Boolean {
            val network = connectivityManager.activeNetwork ?: return false
            val caps = connectivityManager.getNetworkCapabilities(network) ?: return false

            val hasInternet = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val isValidated = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            return hasInternet && isValidated
        }
    }
}