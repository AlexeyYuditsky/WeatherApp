package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R

@Composable
fun WeatherScreenUi(
    connectedUi: ConnectedUi,
    errorUi: ErrorUi,
    weatherUi: WeatherUi,
    retry: () -> Unit,
    goToChooseLocation: () -> Unit,
) = Column {
    connectedUi.Show()
    errorUi.Show(retry)
    IconButton(onClick = goToChooseLocation) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings),
            modifier = Modifier.size(48.dp)
        )
    }
    weatherUi.Show()
}