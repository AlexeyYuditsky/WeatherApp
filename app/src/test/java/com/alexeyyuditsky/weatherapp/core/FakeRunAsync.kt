package com.alexeyyuditsky.weatherapp.core

import com.alexeyyuditsky.weatherapp.core.presentation.RunAsync
import com.alexeyyuditsky.weatherapp.weather.data.WeatherParams
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class FakeRunAsync : RunAsync {

    private var backgroundResult: Any? = null
    private var uiWork: (Any) -> Unit = {}

    private lateinit var queryDebounced: String
    private lateinit var backgroundWorkDebounced: suspend (String) -> Any
    private lateinit var backgroundResultDebounced: Any
    private lateinit var uiWorkDebounced: (Any) -> Unit

    private var mapCached: suspend (WeatherParams) -> WeatherUi = { WeatherUi.Empty }
    private var onEachCached: suspend (Any) -> Unit = {}

    override fun <T : Any, E : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        map: suspend (T) -> E,
        onEach: suspend (E) -> Unit
    ) {
        mapCached = map as suspend (WeatherParams) -> WeatherUi
        onEachCached = onEach as suspend (Any) -> Unit
    }

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
        backgroundWorkDebounced = background
        @Suppress("UNCHECKED_CAST")
        uiWorkDebounced = ui as (Any) -> Unit
    }

    override fun emit(
        query: String,
        isRetryCall: Boolean,
    ) {
        queryDebounced = query
    }

    fun runBackgroundWorkDebounce() = runBlocking {
        backgroundResultDebounced = backgroundWorkDebounced.invoke(queryDebounced)
    }

    fun runUiWorkDebounce() = uiWorkDebounced.invoke(backgroundResultDebounced)

    fun pingFlow(weatherParams: WeatherParams) = runBlocking {
        val result = mapCached.invoke(weatherParams)
        onEachCached.invoke(result)
    }

    fun returnResult() {
        backgroundResult?.let {
            uiWork.invoke(it)
            backgroundResult = null
        }
    }
}