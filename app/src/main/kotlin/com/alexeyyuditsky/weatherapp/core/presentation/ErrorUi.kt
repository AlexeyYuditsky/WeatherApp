package com.alexeyyuditsky.weatherapp.core.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun ErrorUi(
    @StringRes errorResId: Int,
    onRetryClick: () -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = stringResource(errorResId),
        modifier = Modifier.testTag("noInternetConnection")
    )
    Button(
        onClick = onRetryClick,
        modifier = Modifier
            .padding(top = 16.dp)
            .testTag("retryButton")
    ) {
        Text(text = stringResource(R.string.retry))
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewError() = ErrorUi(
    errorResId = R.string.no_internet_connection,
    onRetryClick = {}
)