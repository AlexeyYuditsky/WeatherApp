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
import kotlinx.parcelize.Parcelize

interface WeatherUi : Parcelable {

    @Composable
    fun Show() = Unit

    @Parcelize
    data object Empty : WeatherUi {
        private fun readResolve(): Any = Empty
    }

    @Parcelize
    data class Base(
        private val cityName: String,
        private val temperature: String,
    ) : WeatherUi {

        @Composable
        override fun Show() = Column {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Companion.Center,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .testTag("cityName"),
            )
            Spacer(modifier = Modifier.Companion.height(8.dp))
            Text(
                text = temperature,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Companion.Center,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .testTag("weatherTemperature"),
            )
        }

    }

}