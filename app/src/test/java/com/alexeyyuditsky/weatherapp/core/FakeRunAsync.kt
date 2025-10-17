package com.alexeyyuditsky.weatherapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class FakeRunAsync : RunAsync {

    private var backgroundResult: Any? = null
    private var uiWork: (Any) -> Unit = {}

    private var backgroundDebouncedWork: suspend (String) -> Any = {}
    private var backgroundDebouncedResult: Any? = null
    private var uiDebouncedWork: (Any) -> Unit = {}

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    ) = runBlocking {
        backgroundResult = background.invoke()
        @Suppress("UNCHECKED_CAST")
        uiWork = ui as (Any) -> Unit
    }

    override fun <T : Any> debounce(
        scope: CoroutineScope,
        background: suspend (String) -> T,
        ui: (T) -> Unit,
    ) {
        backgroundDebouncedWork = background
        @Suppress("UNCHECKED_CAST")
        uiDebouncedWork = ui as (Any) -> Unit
    }

    override fun emit(
        query: String,
        isRetryCall: Boolean,
    ) = runBlocking {
        backgroundDebouncedResult = backgroundDebouncedWork.invoke(query)
    }

    fun returnResult() {
        backgroundResult?.let {
            uiWork.invoke(it)
            backgroundResult = null
        }
        backgroundDebouncedResult?.let {
            uiDebouncedWork.invoke(it)
            backgroundDebouncedResult = null
        }
    }
}