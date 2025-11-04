package com.alexeyyuditsky.weatherapp.core

import com.alexeyyuditsky.weatherapp.findCity.presentation.FoundCityUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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

    fun <T> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: () -> Unit = {},
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

    fun <T, E> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        background: suspend (T) -> E,
        ui: (E) -> Unit,
    )

    class Base @Inject constructor() : RunAsync {

        override fun <T, E> runFlow(
            scope: CoroutineScope,
            flow: Flow<T>,
            background: suspend (T) -> E,
            ui: (E) -> Unit,
        ) {
            flow.map(background)
                .flowOn(Dispatchers.IO)
                .onEach(ui)
                .launchIn(scope)
        }

        override fun <T> runAsync(
            scope: CoroutineScope,
            background: suspend () -> T,
            ui: () -> Unit,
        ) {
            scope.launch {
                withContext(Dispatchers.IO) { background.invoke() }
                ui.invoke()
            }
        }

        private val inputFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
        private val retryFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

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
                    val startedState = start.invoke(query)
                    emit(startedState)

                    if (startedState is FoundCityUi.Loading) {
                        val foundCityUi = withContext(Dispatchers.IO) {
                            background(query)
                        }
                        emit(foundCityUi)
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