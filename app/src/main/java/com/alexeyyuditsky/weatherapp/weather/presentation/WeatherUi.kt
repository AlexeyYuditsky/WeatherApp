package com.alexeyyuditsky.weatherapp.weather.presentation

import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alexeyyuditsky.weatherapp.R
import com.alexeyyuditsky.weatherapp.core.presentation.LoadingUi
import kotlinx.parcelize.Parcelize

interface WeatherUi : Parcelable {

    @Composable
    fun Show() = Unit

    @Parcelize
    data object Empty : WeatherUi

    @Parcelize
    data object Loading : WeatherUi {

        @Composable
        override fun Show() = LoadingUi()
    }

    @Parcelize
    data class Success(
        private val cityName: String,
        private val details: String,
        private val imageUrl: String,
        private val time: String,
        private val forecast: List<Pair<String, String>> = emptyList()
    ) : WeatherUi {

        @Composable
        override fun Show() = Column {
            Column {
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("CityName"),
                    textAlign = TextAlign.Center
                )
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.weather_image),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .size(72.dp)
                            .background(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(red = 0f, green = 0.2f, blue = 0f, alpha = 0.2f)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = details,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("WeatherTemperature"),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = time,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                LazyColumn {
                    items(forecast) { (text, url) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 2.dp,
                                    color = LocalContentColor.current,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            AsyncImage(
                                model = url,
                                contentDescription = stringResource(R.string.weather_image),
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(72.dp)
                                    .background(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(
                                            red = 0f,
                                            green = 0.2f,
                                            blue = 0f,
                                            alpha = 0.2f
                                        )
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .testTag("WeatherTemperature"),
                            )
                        }
                    }
                }
            }
        }
    }
}