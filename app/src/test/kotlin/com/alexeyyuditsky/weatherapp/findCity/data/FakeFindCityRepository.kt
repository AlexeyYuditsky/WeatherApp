package com.alexeyyuditsky.weatherapp.findCity.data

import com.alexeyyuditsky.weatherapp.findCity.domain.FindCityRepository
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import com.alexeyyuditsky.weatherapp.findCity.domain.NoInternetException
import com.alexeyyuditsky.weatherapp.findCity.domain.ServiceUnavailableException

class FakeFindCityRepository : FindCityRepository {

    var shouldShowError = true
    private lateinit var savedCity: FoundCity
    val findCityCalledList = mutableListOf<String>()

    override suspend fun findCity(
        query: String,
    ): FoundCityResult {
        findCityCalledList += query
        return when {
            query.isBlank() ->
                error("repository should not accept empty query")

            query == "mo" ->
                FoundCityResult.Error(error = NoInternetException)

            query == "mos" ->
                FoundCityResult.Error(error = ServiceUnavailableException)

            query == "mosc" ->
                FoundCityResult.Empty

            query == "mosco" -> {
                if (shouldShowError)
                    FoundCityResult.Error(error = NoInternetException)
                        .also { shouldShowError = false }
                else
                    FoundCityResult.Success(
                        foundCity = FoundCity(
                            name = "Moscow",
                            latitude = 55.75f,
                            longitude = 37.61f,
                        )
                    )
            }

            query == "moscow" ->
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

    override suspend fun saveFoundCity(foundCity: FoundCity) {
        savedCity = foundCity
    }

    override suspend fun saveFoundCity(
        latitude: Double,
        longitude: Double,
    ) = TODO()
}