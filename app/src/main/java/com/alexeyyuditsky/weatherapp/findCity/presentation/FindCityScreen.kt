package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import java.io.Serializable

@Composable
fun FindCityScreen(
    viewModel: FindCityViewModel,
    navigateToWeatherScreen: () -> Unit,
) {
    val input = rememberSaveable { mutableStateOf("") }
    val foundCityUi = viewModel.state.collectAsStateWithLifecycle()

    FindCityScreenUi(
        input = input.value,
        onInputChange = { text ->
            viewModel.findCity(cityName = text)
            input.value = text
        },
        foundCityUi = foundCityUi.value,
        onFoundCityClick = { foundCity ->
            viewModel.chooseCity(foundCity = foundCity)
            navigateToWeatherScreen.invoke()
        },
    )
}

@Composable
fun FindCityScreenUi(
    input: String,
    onInputChange: (String) -> Unit,
    foundCityUi: FoundCityUi,
    onFoundCityClick: (FoundCity) -> Unit,
) {
    Column {
        BasicTextField(
            modifier = Modifier.testTag("findCityInputField"),
            value = input,
            onValueChange = onInputChange
        )
        foundCityUi.Show(onFoundCityClick = onFoundCityClick)
    }
}

interface FoundCityUi : Serializable {

    @Composable
    fun Show(
        onFoundCityClick: (FoundCity) -> Unit,
    )

    data object Empty : FoundCityUi {

        @Composable
        override fun Show(
            onFoundCityClick: (FoundCity) -> Unit,
        ) = Unit

    }

    data class Base(
        private val foundCity: FoundCity,
    ) : FoundCityUi {

        @Composable
        override fun Show(
            onFoundCityClick: (FoundCity) -> Unit
        ) {
            Button(
                onClick = {
                    onFoundCityClick.invoke(foundCity)
                }
            ) {
                Text(
                    modifier = Modifier.testTag("foundCityUi"),
                    text = foundCity.name,
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewEmptyFindCityScreenUi() {
    FindCityScreenUi(
        input = "",
        onInputChange = {},
        foundCityUi = FoundCityUi.Empty,
        onFoundCityClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNotEmptyFindCityScreenUi() {
    FindCityScreenUi(
        input = "Moscow",
        onInputChange = {},
        foundCityUi = FoundCityUi.Base(
            foundCity = FoundCity(
                name = "Moscow",
                latitude = 55.75,
                longitude = 37.61,
            )
        ),
        onFoundCityClick = {},
    )
}