package com.alexeyyuditsky.weatherapp.findCity.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity

@Composable
fun FindCityScreenUi(
    input: String,
    onValueChange: (String) -> Unit,
    foundCityUi: FoundCityUi,
    onFoundCityClick: (FoundCity) -> Unit,
    onRetryClick: () -> Unit,
    onGetLocationClick: () -> Unit,
) = Column {
    Button(
        onClick = onGetLocationClick,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.use_current_location))
    }
    OutlinedTextField(
        label = { Text(text = stringResource(R.string.city)) },
        value = input,
        onValueChange = onValueChange,
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