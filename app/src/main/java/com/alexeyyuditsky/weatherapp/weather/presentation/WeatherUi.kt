package com.alexeyyuditsky.weatherapp.weather.presentation

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.core.presentation.NoConnectionErrorUi
import kotlinx.parcelize.Parcelize

interface WeatherUi : Parcelable {

    @Composable
    fun Show(
        onRetryClick: () -> Unit = {},
    ) = Unit

    @Parcelize
    data object Empty : WeatherUi

    @Parcelize
    data class Base(
        private val cityName: String,
        private val temperature: String,
    ) : WeatherUi {

        @Composable
        override fun Show(
            onRetryClick: () -> Unit,
        ) = Column {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Companion.Center,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .testTag("cityNameText"),
            )
            Spacer(modifier = Modifier.Companion.height(8.dp))
            Text(
                text = temperature,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Companion.Center,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .testTag("temperatureText"),
            )
        }

    }

    @Parcelize
    data object NoConnectionError : WeatherUi {

        @Composable
        override fun Show(
            onRetryClick: () -> Unit,
        ) = Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            NoConnectionErrorUi(onRetryClick = onRetryClick)
        }

    }

}