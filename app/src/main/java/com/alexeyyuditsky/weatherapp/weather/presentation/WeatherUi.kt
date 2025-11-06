package com.alexeyyuditsky.weatherapp.weather.presentation

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alexeyyuditsky.weatherapp.R
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi
import com.alexeyyuditsky.weatherapp.core.presentation.LoadingUi
import kotlinx.parcelize.Parcelize

interface WeatherUi : Parcelable {

    @Composable
    fun Show() = Unit

    @Parcelize
    data class Success(
        private val cityName: String,
        private val details: String,
        private val imageUrl: String,
        private val time: String,
        private val forecast: List<Pair<String, String>> = emptyList(),
    ) : WeatherUi {

        @Composable
        override fun Show() = Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("CityName")
            )
            Row {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.weather_image),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(size = 72.dp)
                        .background(
                            shape = RoundedCornerShape(size = 8.dp),
                            color = Color(
                                red = 0f,
                                green = 0.2f,
                                blue = 0f,
                                alpha = 0.2f
                            )
                        )
                )
                Column {
                    Text(
                        text = details,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("WeatherTemperature"),
                    )
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
            LazyColumn {
                items(forecast) { (text, url) ->
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = LocalContentColor.current,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = stringResource(R.string.weather_image),
                            modifier = Modifier
                                .padding(all = 8.dp)
                                .size(size = 72.dp)
                                .background(
                                    shape = RoundedCornerShape(size = 8.dp),
                                    color = Color(
                                        red = 0f,
                                        green = 0.2f,
                                        blue = 0f,
                                        alpha = 0.2f
                                    )
                                )
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .testTag("WeatherTemperature"),
                        )
                    }
                }
            }
        }
    }

    @Parcelize
    data object Loading : WeatherUi {

        @Composable
        override fun Show() = LoadingUi()
    }

    @Parcelize
    data object Empty : WeatherUi
}

@Preview(showSystemUi = true)
@Composable
fun PreviewWeatherScreenUiSuccess() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Success(
            cityName = "Moscow",
            details = "Temperature: 9.24°C\nClouds (overcast clouds)\nHumidity: 69.0%\nWind speed: 1.88 m/s\nClouds: 100%\nSunrise: 07:41\nSunset: 16:44\nAir pollution: good",
            imageUrl = "",
            time = "5 min ago (10-Aug-2025)",
            forecast = listOf("Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "")
        ),
        connectionUi = ConnectionUi.Connected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showSystemUi = true)
@Composable
fun PreviewWeatherScreenUiLoading() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Loading,
        connectionUi = ConnectionUi.Connected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showSystemUi = true)
@Composable
fun PreviewWeatherScreenUiDisconnected() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Success(
            cityName = "Moscow",
            details = "Temperature: 9.24°C\nClouds (overcast clouds)\nHumidity: 69.0%\nWind speed: 1.88 m/s\nClouds: 100%\nSunrise: 07:41\nSunset: 16:44\nAir pollution: good",
            imageUrl = "",
            time = "5 min ago (10-Aug-2025)",
            forecast = listOf("Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "")
        ),
        connectionUi = ConnectionUi.Disconnected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showSystemUi = true)
@Composable
fun PreviewWeatherScreenUiError() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Empty,
        connectionUi = ConnectionUi.Connected,
        errorUi = ErrorUi.Error,
        retry = {},
        goToChooseLocation = {},
    )