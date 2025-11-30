package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi

@Composable
fun WeatherScreenUi(
    weatherUi: WeatherUi,
    connectionUi: ConnectionUi,
    errorUi: ErrorUi,
    retry: () -> Unit,
    goToChooseLocation: () -> Unit,
) = Box(modifier = Modifier.fillMaxSize()) {
    Column {
        errorUi.Show(retry = retry)
        IconButton(onClick = goToChooseLocation) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.settings),
                modifier = Modifier.size(48.dp),
            )
        }
        weatherUi.Show()
    }
    connectionUi.Show(modifier = Modifier.align(Alignment.BottomCenter))
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherUiSuccessConnectionUiDisconnectedErrorUiEmpty() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Success(
            cityName = "Moscow",
            details = "Temperature: 9.24°C\nClouds (overcast clouds)\nHumidity: 69.0%\nWind speed: 1.88 m/s\nClouds: 100%\nSunrise: 07:41\nSunset: 16:44\nAir pollution: good",
            imageUrl = "",
            time = "5 min ago (10-Aug-2025)",
            forecast = listOf(
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "",
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "",
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to ""
            )
        ),
        connectionUi = ConnectionUi.Disconnected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showBackground = true)
@Composable
fun PreviewWeatherUiSuccessConnectionUiConnectedAfterDisconnectedErrorUiEmpty() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Success(
            cityName = "Moscow",
            details = "Temperature: 9.24°C\nClouds (overcast clouds)\nHumidity: 69.0%\nWind speed: 1.88 m/s\nClouds: 100%\nSunrise: 07:41\nSunset: 16:44\nAir pollution: good",
            imageUrl = "",
            time = "5 min ago (10-Aug-2025)",
            forecast = listOf(
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "",
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "",
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to ""
            )
        ),
        connectionUi = ConnectionUi.ConnectedAfterDisconnected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showBackground = true)
@Composable
fun PreviewWeatherUiSuccessConnectionUiConnectedErrorUiEmpty() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Success(
            cityName = "Moscow",
            details = "Temperature: 9.24°C\nClouds (overcast clouds)\nHumidity: 69.0%\nWind speed: 1.88 m/s\nClouds: 100%\nSunrise: 07:41\nSunset: 16:44\nAir pollution: good",
            imageUrl = "",
            time = "5 min ago (10-Aug-2025)",
            forecast = listOf(
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "",
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to "",
                "Temperature: 8.98°C\nClouds (overcast clouds)\nHumidity: 73.0%\nWind speed: 2.73 m/s\nClouds: 98%\nTime: 04 нояб. 21:00" to ""
            )
        ),
        connectionUi = ConnectionUi.Connected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUiLoading() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Loading,
        connectionUi = ConnectionUi.Connected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showBackground = true)
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
        connectionUi = ConnectionUi.Connected,
        errorUi = ErrorUi.Empty,
        retry = {},
        goToChooseLocation = {},
    )

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUiError() =
    WeatherScreenUi(
        weatherUi = WeatherUi.Empty,
        connectionUi = ConnectionUi.Connected,
        errorUi = ErrorUi.Error,
        retry = {},
        goToChooseLocation = {},
    )