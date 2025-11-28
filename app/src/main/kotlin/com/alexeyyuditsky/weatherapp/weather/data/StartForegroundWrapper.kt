package com.alexeyyuditsky.weatherapp.weather.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface StartForegroundWrapper {

    fun start()

    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : StartForegroundWrapper {

        private val workManager = WorkManager.getInstance(context)

        override fun start() {
            workManager.enqueueUniquePeriodicWork(
                UNIQUE_NAME, ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                PeriodicWorkRequestBuilder<WeatherWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()
            )
        }

        private companion object {
            const val UNIQUE_NAME = "fetch weather"
        }
    }
}