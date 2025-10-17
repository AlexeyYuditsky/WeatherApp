package com.alexeyyuditsky.weatherapp.weather.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R

interface ErrorUi {

    @Composable
    fun Show(retry: () -> Unit) = Unit

    data object Empty : ErrorUi

    data object Error : ErrorUi {

        @Composable
        override fun Show(retry: () -> Unit) = Button(
            onClick = retry,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.refresh),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}