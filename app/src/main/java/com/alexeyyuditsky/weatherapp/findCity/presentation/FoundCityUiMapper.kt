package com.alexeyyuditsky.weatherapp.findCity.presentation

import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCityResult
import javax.inject.Inject

class FoundCityUiMapper @Inject constructor() : FoundCityResult.Mapper<FoundCityUi> {

    override fun mapFoundCity(fountCity: FoundCity): FoundCityUi =
        FoundCityUi.Base(foundCity = fountCity)

    override fun mapEmpty(): FoundCityUi =
        FoundCityUi.Empty

    override fun mapNoConnection(): FoundCityUi =
        FoundCityUi.NoConnectionError

}