package com.alexeyyuditsky.weatherapp.weather.presentation

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.core.presentation.LoadingUi
import com.alexeyyuditsky.weatherapp.core.presentation.NoConnectionErrorUi
import kotlinx.parcelize.Parcelize

interface WeatherUi : Parcelable {

    @Composable
    fun Show(
        onRetryClick: () -> Unit = {},
    ) = Unit

    @Parcelize
    data class Success(
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
    data object Loading : WeatherUi {

        @Composable
        override fun Show(
            onRetryClick: () -> Unit,
        ) = LoadingUi()
    }

    @Parcelize
    data object Empty : WeatherUi

    @Parcelize
    data object NoConnectionError : WeatherUi {

        @Composable
        override fun Show(
            onRetryClick: () -> Unit,
        ) = NoConnectionErrorUi(onRetryClick = onRetryClick)
    }
}