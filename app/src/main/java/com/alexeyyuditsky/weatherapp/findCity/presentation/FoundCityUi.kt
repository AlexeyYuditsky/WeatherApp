package com.alexeyyuditsky.weatherapp.findCity.presentation

import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.alexeyyuditsky.weatherapp.findCity.domain.FoundCity
import kotlinx.parcelize.Parcelize

interface FoundCityUi : Parcelable {

    @Composable
    fun Show(
        onFoundCityClick: (FoundCity) -> Unit,
    ) = Unit

    @Parcelize
    data object Empty : FoundCityUi {
        private fun readResolve(): Any = Empty
    }

    @Parcelize
    data class Base(
        private val foundCity: FoundCity,
    ) : FoundCityUi {

        @Composable
        override fun Show(
            onFoundCityClick: (FoundCity) -> Unit,
        ) = Button(
            onClick = { onFoundCityClick.invoke(foundCity) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = foundCity.name,
                modifier = Modifier
                    .testTag("foundCityUi"),
            )
        }

    }

}