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

    enum class Status { Connected, ConnectedAfterDisconnected, Disconnected }

    @Singleton
    class Base @Inject constructor(@ApplicationContext context: Context) : Connection {

        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        private val isConnectedAtStart: Boolean = isConnectedAtStart()

        private val networkStatus: Flow<Boolean> = callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(true).also { log("Connection: onAvailable -> true") }
                }

                override fun onLost(network: Network) {
                    trySend(false).also { log("Connection: onLost -> false") }
                }
            }

            trySend(isConnectedAtStart)
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }

        override val initialStatus: Status =
            if (isConnectedAtStart) Status.Connected else Status.Disconnected

        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        override val statuses: Flow<Status> =
            networkStatus
                .debounce(300)
                .distinctUntilChanged()
                .let { flow ->
                    var wasDisconnected = !isConnectedAtStart

                    flow.transformLatest { isConnected ->
                        if (!isConnected) {
                            delay(1000)
                            wasDisconnected = true
                            log("Connection: emit Disconnected")
                            emit(Status.Disconnected)
                        } else {
                            if (wasDisconnected) {
                                log("Connection: emit ConnectedAfterDisconnected")
                                emit(Status.ConnectedAfterDisconnected)
                                delay(1000)
                            }
                            log("Connection: emit Connected")
                            emit(Status.Connected)
                            wasDisconnected = false
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