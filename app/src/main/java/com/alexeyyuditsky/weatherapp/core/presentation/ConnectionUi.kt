package com.alexeyyuditsky.weatherapp.core.presentation

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R

interface ConnectionUi {

    @Composable
    fun Show() = Unit

    data object Connected : ConnectionUi

    data object Disconnected : ConnectionUi {

        @Composable
        override fun Show() = Text(
            text = stringResource(R.string.no_internet_connection),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red)
                .padding(all = 8.dp)
                .testTag("noInternetConnection")
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ConnectedPreview() = ConnectionUi.Connected.Show()

@Preview(showSystemUi = true)
@Composable
fun DisconnectedPreview() = ConnectionUi.Disconnected.Show()