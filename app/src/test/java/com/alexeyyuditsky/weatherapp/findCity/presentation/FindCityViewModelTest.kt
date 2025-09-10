package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FindCityViewModelTest {

    private val repository = FakeFindCityRepository()
    private val savedStateHandle = SavedStateHandle()
    private val runAsync = FakeRunAsync()
    private val findCityViewModel = FindCityViewModel(
        savedStateHandle = savedStateHandle,
        findCityRepository = repository,
        runAsync = runAsync
    )

    @Test
    fun findCityAndSaveIt() {
        val expected: FoundCityUi = FoundCityUi.Empty
        val actual: FoundCityUi = findCityViewModel.state.value
        assertEquals(expected, actual)

        findCityViewModel.findCity(cityName = "Mos")
        assertEquals(expected, actual)

        runAsync.returnResult()
        val foundCity = FoundCity(
            name = "Moscow",
            latitude = 55.75,
            longitude = 37.61,
        )
        val expected2: FoundCityUi = FoundCityUi.Base(foundCity = foundCity)
        val actual2: FoundCityUi = findCityViewModel.state.value
        assertEquals(expected2, actual2)

        findCityViewModel.chooseCity(foundCity = foundCity)
        repository.assertSaveCalled(expected = foundCity)
    }

}

private class FakeFindCityRepository : FindCityRepository {

    private lateinit var savedCity: FoundCity

    override suspend fun findCity(query: String): FoundCity {
        if (query == "Mos") return FoundCity(
            name = "Moscow",
            latitude = 55.75,
            longitude = 37.61,
        )

        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        savedCity = foundCity
    }

    fun assertSaveCalled(expected: FoundCity) =
        assertEquals(expected, savedCity)

}

class FakeRunAsync : RunAsync {

    private lateinit var resultCached: Any
    private lateinit var uiCached: (Any) -> Unit

    override suspend fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit,
    ) = runBlocking {
        resultCached = background.invoke()
        uiCached = ui as (Any) -> Unit
    }

    fun returnResult() =
        uiCached.invoke(resultCached)

}