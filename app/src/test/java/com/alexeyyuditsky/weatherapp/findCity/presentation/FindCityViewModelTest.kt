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
    fun runEmptyQueriesThenGetNoInternetErrorThenGetServiceUnavailableErrorThenGetEmptyThenGetFoundCity() {
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = "")
        assertEquals(emptyList<String>(), repository.findCityCalledList)
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = " ")
        assertEquals(emptyList<String>(), repository.findCityCalledList)
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = "mo")
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo"), repository.findCityCalledList)
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runUiWorkDebounce()
        assertEquals(FoundCityUi.NoConnectionError, viewModel.state.value)

        viewModel.findCity(cityName = "mos")
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo", "mos"), repository.findCityCalledList)
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runUiWorkDebounce()
        assertEquals(FoundCityUi.ServiceUnavailableError, viewModel.state.value)

        viewModel.findCity(cityName = "mosc")
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo", "mos", "mosc"), repository.findCityCalledList)
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
        runAsync.runUiWorkDebounce()
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.findCity(cityName = "moscow")
        runAsync.runBackgroundWorkDebounce()
        assertEquals(listOf("mo", "mos", "mosc", "moscow"), repository.findCityCalledList)
        assertEquals(FoundCityUi.Loading, viewModel.state.value)
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