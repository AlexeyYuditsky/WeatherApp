package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity

@Composable
fun FindCityScreenUi(
    input: String,
    onInputChange: (String) -> Unit,
    foundCityUi: FoundCityUi,
    onFoundCityClick: (FoundCity) -> Unit,
    onRetryClick: () -> Unit,
) = Column {
    OutlinedTextField(
        label = { Text(text = stringResource(R.string.city)) },
        value = input,
        onValueChange = onInputChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("outlinedTextField"),
    )
    foundCityUi.Show(
        onFoundCityClick = onFoundCityClick,
        onRetryClick = onRetryClick
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiSuccess() = FindCityScreenUi(
    input = "Moscow",
    onInputChange = {},
    foundCityUi = FoundCityUi.Success(
        foundCity = FoundCity(
            name = "Moscow",
            latitude = 55.75f,
            longitude = 37.61f,
        )
    ),
    onFoundCityClick = {},
    onRetryClick = {},
)

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiLoading() = FindCityScreenUi(
    input = "Moscow",
    onInputChange = {},
    foundCityUi = FoundCityUi.Loading,
    onFoundCityClick = {},
    onRetryClick = {},
)

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiEmpty() = FindCityScreenUi(
    input = "",
    onInputChange = {},
    foundCityUi = FoundCityUi.Empty,
    onFoundCityClick = {},
    onRetryClick = {},
)

@Preview(showBackground = true)
@Composable
private fun PreviewFindCityScreenUiNoConnectionError() = FindCityScreenUi(
    input = "Moscow",
    onInputChange = {},
    foundCityUi = FoundCityUi.NoConnectionError,
    onFoundCityClick = {},
    onRetryClick = {},
)