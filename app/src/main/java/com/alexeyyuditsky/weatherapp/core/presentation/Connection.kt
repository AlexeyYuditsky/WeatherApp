package com.alexeyyuditsky.weatherapp.core.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface Connection {

    val connected: StateFlow<Boolean>

    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : Connection {

        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        private val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        private val mutableConnection = MutableStateFlow(isConnectedNow())
        override val connected = mutableConnection.asStateFlow()

        init {
            connectivityManager.registerNetworkCallback(request, object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    mutableConnection.value = true
                }

                override fun onLost(network: Network) {
                    mutableConnection.value = false
                }
            })
        }

        private fun isConnectedNow(): Boolean = with(connectivityManager) {
            getNetworkCapabilities(activeNetwork)?.run {
                hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } ?: false
        }
    }
}