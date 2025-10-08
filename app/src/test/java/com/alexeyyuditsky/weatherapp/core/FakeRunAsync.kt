package com.alexeyyuditsky.weatherapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class FakeRunAsync : RunAsync{

    private var resultCached: Any? = null
    private var uiCached: (Any) -> Unit = {}

    override fun <T : Any> run(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    ) = runBlocking {
        resultCached = background.invoke()
        @Suppress("UNCHECKED_CAST")
        uiCached = ui as (Any) -> Unit
    }

    private var backgroundDebounced: suspend (String) -> Any = {}
    private var uiDebounced: (Any) -> Unit = {}
    private var debouncedResult: Any? = null

    override fun <T : Any> debounce(
        scope: CoroutineScope,
        background: suspend (String) -> T,
        ui: (T) -> Unit,
    ) {
        backgroundDebounced = background
        @Suppress("UNCHECKED_CAST")
        uiDebounced = ui as (Any) -> Unit
    }

    override fun emitInput(query: String) = runBlocking {
        debouncedResult = backgroundDebounced.invoke(query)
    }

    override fun emitRetry(query: String) = runBlocking {
        debouncedResult = backgroundDebounced.invoke(query)
    }

    fun returnResult() {
        resultCached?.let {
            uiCached.invoke(it)
            resultCached = null
        }
        debouncedResult?.let {
            uiDebounced.invoke(it)
            debouncedResult = null
        }
    }
}