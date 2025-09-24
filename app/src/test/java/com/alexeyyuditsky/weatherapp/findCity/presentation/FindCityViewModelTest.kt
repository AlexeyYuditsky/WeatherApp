package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.lifecycle.SavedStateHandle
import com.alexeyyuditsky.weatherapp.core.FakeRunAsync
import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import org.junit.Assert.assertEquals
import org.junit.Test

class FindCityViewModelTest {

    private val repository = FakeFindCityRepository()
    private val savedStateHandle = SavedStateHandle()
    private val runAsync = FakeRunAsync()
    private val findCityViewModel = FindCityViewModel(
        savedStateHandle = savedStateHandle,
        findCityRepository = repository,
        runAsync = runAsync,
    )

    @Test
    fun findCityAndSaveIt() {
        val expected: FoundCityUi = FoundCityUi.Empty
        val actual: FoundCityUi = findCityViewModel.state.value
        assertEquals(expected, actual)

        findCityViewModel.findCity(cityName = "Mos")
        runAsync.returnResult()
        val foundCity = FoundCity(
            name = "Moscow",
            latitude = 55.75f,
            longitude = 37.61f,
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

    override suspend fun findCity(query: String): FoundCity = if (query == "Mos")
        FoundCity(
            name = "Moscow",
            latitude = 55.75f,
            longitude = 37.61f,
        ) else
        throw IllegalStateException("not supported for this test")

    override suspend fun saveCity(foundCity: FoundCity) {
        savedCity = foundCity
    }

    fun assertSaveCalled(expected: FoundCity) = assertEquals(expected, savedCity)

}