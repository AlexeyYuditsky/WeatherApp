package com.alexeyyuditsky.weatherapp.core

import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RunAsync {

    fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit = {},
    )

    fun debounce(
        scope: CoroutineScope,
        start: (String) -> FoundCityUi,
        background: suspend (String) -> FoundCityUi,
        ui: (FoundCityUi) -> Unit,
    )

    fun emit(
        query: String,
        isRetryCall: Boolean,
    )

    fun <T : Any, E : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        map: suspend (T) -> E,
        onEach: suspend (E) -> Unit,
    )

    class Base @Inject constructor() : RunAsync {

        override fun <T : Any, E : Any> runFlow(
            scope: CoroutineScope,
            flow: Flow<T>,
            map: suspend (T) -> E,
            onEach: suspend (E) -> Unit,
        ) {
            flow.map(map)
                .onEach(onEach)
                .flowOn(Dispatchers.IO)
                .launchIn(scope)
        }

        override fun <T : Any> runAsync(
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

        private val inputFlow = MutableSharedFlow<String>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        private val retryFlow = MutableSharedFlow<String>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        override fun debounce(
            scope: CoroutineScope,
            start: (String) -> FoundCityUi,
            background: suspend (String) -> FoundCityUi,
            ui: (FoundCityUi) -> Unit,
        ) {
            val input = inputFlow
                .map { query -> query.trim() }
                .distinctUntilChanged()
                .debounce(1000)

            merge(input, retryFlow)
                .transformLatest { query ->
                    val startState = start.invoke(query)
                    emit(startState)

                    if (startState is FoundCityUi.Loading) {
                        emit(
                            withContext(Dispatchers.IO) {
                                background(query)
                            }
                        )
                    }
                }
                .onEach(ui)
                .launchIn(scope)
        }

        override fun emit(
            query: String,
            isRetryCall: Boolean,
        ) {
            if (isRetryCall)
                retryFlow.tryEmit(query)
            else
                inputFlow.tryEmit(query)
        }
    }
}