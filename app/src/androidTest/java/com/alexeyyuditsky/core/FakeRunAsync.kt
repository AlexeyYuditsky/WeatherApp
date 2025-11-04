package com.alexeyyuditsky.core

import com.alexeyyuditsky.weatherapp.core.RunAsync
import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

@Suppress("UNCHECKED_CAST")
class FakeRunAsync : RunAsync {

    private var resultCached: Any? = null
    private var uiCached: (Any) -> Unit = {}

    override fun <T> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: () -> Unit
    ) {
        runBlocking {
            val result: T = background.invoke()
            resultCached = result
            uiCached = ui as (Any) -> Unit
        }
    }

    private var backgroundDebounced: suspend (String) -> Any = {}
    private var uiDebounced: (Any) -> Unit = {}
    private var debouncedResult: Any? = null

    override fun debounce(
        scope: CoroutineScope,
        start: (String) -> FoundCityUi,
        background: suspend (String) -> FoundCityUi,
        ui: (FoundCityUi) -> Unit
    ) {
        backgroundDebounced = background
        uiDebounced = ui as (Any) -> Unit
    }

    override fun emit(
        query: String,
        isRetryCall: Boolean,
    ) = runBlocking {
        debouncedResult = backgroundDebounced.invoke(query)
    }

    override fun <T : Any, E : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        background: suspend (T) -> E,
        ui: suspend (E) -> Unit
    ) {
        flow
            .map(background)
            .onEach(ui)
            .launchIn(scope)
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