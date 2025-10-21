package com.alexeyyuditsky.weatherapp.core.data

import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import com.alexeyyuditsky.weatherapp.findCity.domain.ServiceUnavailableException
import java.io.IOException

interface CloudDataSource {

    suspend fun <T> handle(
        block: suspend () -> T,
    ): T = try {
        block.invoke()
    } catch (e: Exception) {
        if (e is IOException)
            throw NoInternetException
        else
            throw ServiceUnavailableException
    }
}