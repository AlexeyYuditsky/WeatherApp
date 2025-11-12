package com.alexeyyuditsky.weatherapp.core.presentation

import androidx.annotation.StringRes
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
    fun Show(modifier: Modifier = Modifier) = Unit

    data object Connected : ConnectionUi

    data object ConnectedAfterDisconnected : ConnectionUi {

        @Composable
        override fun Show(modifier: Modifier) =
            ConnectionContainer(
                modifier = modifier.testTag("connectedAfterDisconnected"),
                text = R.string.connection_restored,
                color = Color.Green,
            )
    }

    data object Disconnected : ConnectionUi {

        @Composable
        override fun Show(modifier: Modifier) =
            ConnectionContainer(
                modifier = modifier.testTag("disconnected"),
                text = R.string.no_internet_connection,
                color = Color.Black,
            )
    }
}

@Composable
fun ConnectionContainer(
    modifier: Modifier,
    color: Color,
    @StringRes text: Int,
) = Text(
    text = stringResource(text),
    textAlign = TextAlign.Center,
    color = Color.White,
    style = MaterialTheme.typography.titleMedium,
    modifier = modifier
        .fillMaxWidth()
        .background(color = color)
        .padding(all = 8.dp)
)

@Preview(showBackground = true)
@Composable
fun ConnectedPreview() = ConnectionUi.Connected.Show()

@Preview(showBackground = true)
@Composable
fun ConnectedAfterDisconnectedPreview() = ConnectionUi.ConnectedAfterDisconnected.Show()

@Preview(showBackground = true)
@Composable
fun DisconnectedPreview() = ConnectionUi.Disconnected.Show()