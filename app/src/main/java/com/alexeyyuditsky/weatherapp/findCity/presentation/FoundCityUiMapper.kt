package com.alexeyyuditsky.weatherapp.findCity.presentation

import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import javax.inject.Inject

class FoundCityUiMapper @Inject constructor() : FoundCityResult.Mapper<FoundCityUi> {

    override fun mapToSuccess(fountCity: FoundCity): FoundCityUi =
        FoundCityUi.Success(foundCity = fountCity)

    override fun mapToLoading(): FoundCityUi =
        FoundCityUi.Loading

    override fun mapToEmpty(): FoundCityUi =
        FoundCityUi.Empty

    override fun mapToNoConnectionError(): FoundCityUi =
        FoundCityUi.NoConnectionError
}