package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R

@Composable
fun WeatherScreenUi(
    weatherUi: WeatherUi,
    connectedUi: ConnectedUi,
    errorUi: ErrorUi,
    retry: () -> Unit,
    goToChooseLocation: () -> Unit,
) = Column {
    connectedUi.Show()
    errorUi.Show(retry)
    IconButton(onClick = goToChooseLocation) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.settings),
            modifier = Modifier.size(size = 48.dp)
        )
    }
    weatherUi.Show()
}