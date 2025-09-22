package com.alexeyyuditsky.weatherapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface RunAsync {

    operator fun <T> invoke(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit = {},
    )

    @Singleton
    class Base @Inject constructor() : RunAsync {

        override fun <T> invoke(
            scope: CoroutineScope,
            background: suspend () -> T,
            ui: (T) -> Unit,
        ) {
            scope.launch(Dispatchers.IO) {
                val result = background.invoke()
                withContext(Dispatchers.Main) {
                    ui.invoke(result)
                }
            }
        }

    }

}