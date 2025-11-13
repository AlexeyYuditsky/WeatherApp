package com.alexeyyuditsky.weatherapp.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alexeyyuditsky.weatherapp.R

@Composable
fun LoadingUi() = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxWidth()
) {
    CircularProgressIndicator()
    Text(
        text = stringResource(R.string.loading),
        modifier = Modifier.testTag("circularProgress")
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoading() = LoadingUi()