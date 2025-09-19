package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexeyyuditsky.weatherapp.R
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
        OutlinedTextField(
            label = { Text(text = stringResource(R.string.city)) },
            value = input,
            onValueChange = onInputChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("findCityInputField"),
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

        private fun readResolve(): Any = Empty

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                latitude = 55.75f,
                longitude = 37.61f,
            )
        ),
        onFoundCityClick = {},
    )
}