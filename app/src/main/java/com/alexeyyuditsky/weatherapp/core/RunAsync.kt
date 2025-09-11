package com.alexeyyuditsky.weatherapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {

    operator fun <T : Any> invoke(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit = {},
    )

    class Base : RunAsync {

        override fun <T : Any> invoke(
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