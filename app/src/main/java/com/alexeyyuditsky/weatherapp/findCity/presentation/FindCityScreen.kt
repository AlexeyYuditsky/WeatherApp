package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity

@Composable
fun FindCityScreen(
    viewModel: FindCityViewModel,
    navigateToWeatherScreen: () -> Unit,
    onGetLocationClick: () -> Unit,
) {
    var input by rememberSaveable { mutableStateOf("") }
    val foundCityUi by viewModel.state.collectAsStateWithLifecycle()
    val close by viewModel.close.collectAsState()

    if (close) SideEffect {
        navigateToWeatherScreen.invoke()
    }

    FindCityScreenUi(
        input = input,
        onValueChange = { text ->
            viewModel.findCity(cityName = text)
            input = text
        },
        foundCityUi = foundCityUi,
        onFoundCityClick = { foundCity ->
            viewModel.chooseCity(foundCity = foundCity)
        },
        onRetryClick = {
            viewModel.findCity(cityName = input, isRetryCall = true)
        },
        onGetLocationClick = onGetLocationClick
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiSuccess() = FindCityScreenUi(
    input = "Moscow",
    onValueChange = {},
    foundCityUi = FoundCityUi.Success(
        foundCity = FoundCity(
            name = "Moscow",
            latitude = 55.75f,
            longitude = 37.61f,
        )
    ),
    onFoundCityClick = {},
    onRetryClick = {},
    onGetLocationClick = {},
)

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiLoading() = FindCityScreenUi(
    input = "Moscow",
    onValueChange = {},
    foundCityUi = FoundCityUi.Loading,
    onFoundCityClick = {},
    onRetryClick = {},
    onGetLocationClick = {},
)

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiEmpty() = FindCityScreenUi(
    input = "",
    onValueChange = {},
    foundCityUi = FoundCityUi.Empty,
    onFoundCityClick = {},
    onRetryClick = {},
    onGetLocationClick = {},
)

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiNoConnectionError() = FindCityScreenUi(
    input = "Moscow",
    onValueChange = {},
    foundCityUi = FoundCityUi.NoConnectionError,
    onFoundCityClick = {},
    onRetryClick = {},
    onGetLocationClick = {},
)