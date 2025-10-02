package com.alexeyyuditsky.weatherapp.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.R

@Composable
fun NoConnectionErrorUi(
    onRetryClick: () -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxWidth(),
) {
    Text(
        text = stringResource(R.string.no_internet_connection),
        modifier = Modifier.testTag("noInternetConnectionText"),
    )
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = onRetryClick,
        modifier = Modifier.testTag("retryButton"),
    ) {
        Text(text = stringResource(R.string.retry))
    }
}

@Preview(showBackground = true)
@Composable
fun ShowNoConnectionError() = NoConnectionErrorUi { }