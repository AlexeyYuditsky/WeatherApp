package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R
import com.alexeyyuditsky.weatherapp.core.presentation.ConnectionUi
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity

@Composable
fun FindCityScreenUi(
    connectionUi: ConnectionUi,
    input: String,
    onValueChange: (String) -> Unit,
    foundCityUi: FoundCityUi,
    onFoundCityClick: (FoundCity) -> Unit,
    onRetryClick: () -> Unit,
    onGetLocationClick: () -> Unit,
) = Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Button(
            onClick = onGetLocationClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.use_current_location))
        }
        OutlinedTextField(
            label = { Text(text = stringResource(R.string.city)) },
            value = input,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("outlinedTextField"),
        )
        foundCityUi.Show(
            onFoundCityClick = onFoundCityClick,
            onRetryClick = onRetryClick
        )
    }
    connectionUi.Show(modifier = Modifier.align(Alignment.BottomCenter))
}

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiSuccessAndNoInternetConnection() = FindCityScreenUi(
    connectionUi = ConnectionUi.Disconnected,
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
    connectionUi = ConnectionUi.Connected,
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
    connectionUi = ConnectionUi.Connected,
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
    connectionUi = ConnectionUi.Connected,
    input = "Moscow",
    onValueChange = {},
    foundCityUi = FoundCityUi.NoConnectionError,
    onFoundCityClick = {},
    onRetryClick = {},
    onGetLocationClick = {},
)

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiServiceUnavailableError() = FindCityScreenUi(
    connectionUi = ConnectionUi.Connected,
    input = "Moscow",
    onValueChange = {},
    foundCityUi = FoundCityUi.ServiceUnavailableError,
    onFoundCityClick = {},
    onRetryClick = {},
    onGetLocationClick = {},
)