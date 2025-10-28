package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import com.alexeyyuditsky.weatherapp.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.findCity.data.FakeFindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import org.junit.Assert.assertEquals
import org.junit.Test

class FindCityViewModelTest {

    private val repository = FakeFindCityRepository()
    private val runAsync = FakeRunAsync()
    private val viewModel = FindCityViewModel(
        savedStateHandle = SavedStateHandle(),
        repository = repository,
        runAsync = runAsync,
        mapper = FoundCityUiMapper(),
    )

    @Test
    fun run_empty_queries_then_get_no_internet_error_then_get_service_unavailable_error_then_get_empty_then_get_FoundCity_then_get_FoundCity_with_whitespace_then_run_empty_queries() {
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = "")
        assertEquals(emptyList<String>(), repository.findCityCalledList)
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = " ")
        assertEquals(emptyList<String>(), repository.findCityCalledList)
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = "mo")
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo"), repository.findCityCalledList)
        runAsync.runUiWorkDebounce()
        assertEquals(FoundCityUi.NoConnectionError, viewModel.state.value)

        viewModel.findCity(cityName = "mos")
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo", "mos"), repository.findCityCalledList)
        runAsync.runUiWorkDebounce()
        assertEquals(FoundCityUi.ServiceUnavailableError, viewModel.state.value)

        viewModel.findCity(cityName = "mosc")
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo", "mos", "mosc"), repository.findCityCalledList)
        runAsync.runUiWorkDebounce()
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = "moscow")
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo", "mos", "mosc", "moscow"), repository.findCityCalledList)
        runAsync.runUiWorkDebounce()
        assertEquals(
            FoundCityUi.Success(
                foundCity = FoundCity(
                    name = "Moscow",
                    latitude = 55.75f,
                    longitude = 37.61f,
                )
            ),
            viewModel.state.value
        )

        viewModel.findCity(cityName = "moscow ")
        assertEquals(
            FoundCityUi.Success(
                foundCity = FoundCity(
                    name = "Moscow",
                    latitude = 55.75f,
                    longitude = 37.61f,
                )
            ),
            viewModel.state.value
        )

        viewModel.findCity(cityName = "")
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = " ")
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Empty, viewModel.state.value)
    }

    @Test
    fun get_no_internet_error_then_click_retry_get_FoundCity() {
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = "mosco")
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mosco"), repository.findCityCalledList)
        runAsync.runUiWorkDebounce()
        assertEquals(FoundCityUi.NoConnectionError, viewModel.state.value)

        viewModel.findCity(cityName = "mosco", isRetryCall = true)
        runAsync.runStartWorkDebounced()
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mosco", "mosco"), repository.findCityCalledList)
        runAsync.runUiWorkDebounce()
        assertEquals(
            FoundCityUi.Success(
                foundCity = FoundCity(
                    name = "Moscow",
                    latitude = 55.75f,
                    longitude = 37.61f,
                )
            ),
            viewModel.state.value
        )
    }
}