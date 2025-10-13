package com.alexeyyuditsky.weatherapp.findCity.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun FindCityOrGetLocationScreen(
    viewModel: FindCityViewModel,
    navigateToWeatherScreen: () -> Unit,
) {
    val context = LocalContext.current
    var getLocation by rememberSaveable { mutableStateOf(false) }
    if (getLocation)
        GetUserLocationScreenWrapper(
            onLocationProvided = { lat, lon ->
                viewModel.chooseLocation(lat, lon)
                getLocation = false
                navigateToWeatherScreen.invoke()
            },
            onFailed = { text ->
                Toast.makeText(context.applicationContext, text, Toast.LENGTH_SHORT).show()
                getLocation = false
            }
        )

    FindCityScreen(
        viewModel = viewModel,
        navigateToWeatherScreen = navigateToWeatherScreen,
        onGetLocationClick = { getLocation = true }
    )
}