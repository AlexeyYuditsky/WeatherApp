package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.io.Serializable

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
) {
    val weatherScreenUi = viewModel.state.collectAsStateWithLifecycle()
    weatherScreenUi.value.Show()
}

interface WeatherScreenUi : Serializable {

    @Composable
    fun Show() = Unit

    data object Empty : WeatherScreenUi {

        private fun readResolve(): Any = Empty

    }

    data class Base(
        private val cityName: String,
        private val temperature: String,
    ) : WeatherScreenUi {

        @Composable
        override fun Show() {
            Column {
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cityName"),
                )
                Spacer(
                    modifier = Modifier.height(8.dp),
                )
                Text(
                    text = temperature,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("weatherTemperature"),
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUi() = WeatherScreenUi.Base(
    cityName = "Moscow city",
    temperature = "33.1°C",
).Show()