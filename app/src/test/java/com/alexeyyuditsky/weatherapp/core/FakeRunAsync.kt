package com.alexeyyuditsky.weatherapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class FakeRunAsync : RunAsync {

    private lateinit var resultCached: Any
    private lateinit var uiCached: (Any) -> Unit

    override operator fun <T : Any> invoke(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    ) = runBlocking {
        resultCached = background.invoke()
        @Suppress("UNCHECKED_CAST")
        uiCached = ui as (Any) -> Unit
    }

    fun returnResult() =
        uiCached.invoke(resultCached)
}