package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R

interface ConnectedUi {

    @Composable
    fun Show() = Unit

    data object Connected : ConnectedUi

    data object Disconnected : ConnectedUi {

        @Composable
        override fun Show() = Text(
            text = stringResource(R.string.no_internet_connection),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red)
                .padding(vertical = 16.dp)
                .testTag("noInternetConnection")
        )
    }
}