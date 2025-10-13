package com.alexeyyuditsky.weatherapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexeyyuditsky.weatherapp.core.Routes
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityOrGetLocationScreen
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityViewModel
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherScreen
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherViewModel

@SuppressLint("MissingPermission")
@Composable
fun MainContent(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.FIND_CITY,
        modifier = Modifier.Companion.padding(paddingValues = innerPadding)
    ) {
        composable(route = Routes.FIND_CITY) {
            FindCityOrGetLocationScreen(
                viewModel = hiltViewModel<FindCityViewModel>(),
                navigateToWeatherScreen = { navController.navigate(Routes.WEATHER) },
            )
        }

        composable(route = Routes.WEATHER) {
            WeatherScreen(
                viewModel = hiltViewModel<WeatherViewModel>(),
            )
        }
    }
}