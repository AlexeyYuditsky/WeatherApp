package com.alexeyyuditsky.weatherapp.findCity.presentation

import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import javax.inject.Inject

class FoundCityUiMapper @Inject constructor() : FoundCityResult.Mapper<FoundCityUi> {

    override fun mapToSuccess(foundCity: FoundCity): FoundCityUi =
        FoundCityUi.Success(foundCity = foundCity)

    override fun mapToEmpty(): FoundCityUi =
        FoundCityUi.Empty

    override fun mapToNoConnectionError(): FoundCityUi =
        FoundCityUi.NoConnectionError

    override fun mapToServiceUnavailableError(): FoundCityUi =
        FoundCityUi.ServiceUnavailableError
}