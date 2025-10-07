package com.alexeyyuditsky.weatherapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RunAsync<R> {

    fun <T : Any> run(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit = {},
    )

    fun <T : Any> debounce(
        scope: CoroutineScope,
        background: suspend (R) -> T,
        ui: (T) -> Unit,
    )

    suspend fun emit(value: R)

    class Base @Inject constructor() : RunAsync<QueryEvent> {

        override fun <T : Any> run(
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

        private val inputFlow = MutableSharedFlow<QueryEvent>()

        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        override fun <T : Any> debounce(
            scope: CoroutineScope,
            background: suspend (QueryEvent) -> T,
            ui: (T) -> Unit,
        ) {
            inputFlow
                .debounce(500)
                .mapLatest { latestQuery -> background.invoke(latestQuery) }
                .flowOn(Dispatchers.IO)
                .onEach(ui)
                .launchIn(scope)
        }

        override suspend fun emit(value: QueryEvent) {
            inputFlow.emit(value)
        }
    }
}