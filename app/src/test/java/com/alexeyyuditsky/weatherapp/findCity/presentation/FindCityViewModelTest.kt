package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import com.alexeyyuditsky.weatherapp.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
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
    fun getErrorThenInputFindCityThenSaveIt() {
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.inputFindCity(cityName = "")
        repository.assertFindCityCalled(emptyList())
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.inputFindCity(cityName = " ")
        repository.assertFindCityCalled(emptyList())
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.inputFindCity(cityName = "Mo")
        repository.assertFindCityCalled(listOf("Mo"))
        assertEquals(FoundCityUi.Loading, viewModel.state.value)

        runAsync.returnResult()
        assertEquals(FoundCityUi.NoConnectionError, viewModel.state.value)

        viewModel.inputFindCity(cityName = "Mo")
        repository.assertFindCityCalled(listOf("Mo", "Mo"))
        assertEquals(FoundCityUi.Loading, viewModel.state.value)

        runAsync.returnResult()
        assertEquals(FoundCityUi.Empty, viewModel.state.value)

        viewModel.inputFindCity(cityName = "Mos")
        repository.assertFindCityCalled(listOf("Mo", "Mo", "Mos"))
        assertEquals(FoundCityUi.Loading, viewModel.state.value)

        runAsync.returnResult()
        val foundCity = FoundCity(
            name = "Moscow",
            latitude = 55.75f,
            longitude = 37.61f,
        )
        assertEquals(
            FoundCityUi.Success(foundCity = foundCity),
            viewModel.state.value
        )

        viewModel.chooseCity(foundCity = foundCity)
        repository.assertSaveCalled(expected = foundCity)
    }
}

private class FakeFindCityRepository : FindCityRepository {

    private val findCityCalledList = mutableListOf<String>()
    private var shouldShowError = true
    private lateinit var savedCity: FoundCity

    override suspend fun findCity(query: String): FoundCityResult {
        findCityCalledList += query

        return when {
            query.isBlank() ->
                error("repository should not accept empty query")

            query == "Mo" -> {
                if (shouldShowError)
                    FoundCityResult.Error(error = NoInternetException)
                        .also { shouldShowError = false }
                else
                    FoundCityResult.Empty
            }

            query == "Mos" ->
                FoundCityResult.Success(
                    foundCity = FoundCity(
                        name = "Moscow",
                        latitude = 55.75f,
                        longitude = 37.61f,
                    )
                )

            else ->
                error("not supported for this test")
        }
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        savedCity = foundCity
    }

    fun assertSaveCalled(expected: FoundCity) =
        assertEquals(expected, savedCity)

    fun assertFindCityCalled(expected: List<String>) =
        assertEquals(expected, findCityCalledList)
}