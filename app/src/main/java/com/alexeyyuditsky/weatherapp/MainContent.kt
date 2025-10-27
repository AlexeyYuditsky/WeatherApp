package com.alexeyyuditsky.weatherapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexeyyuditsky.weatherapp.core.Screen
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityOrGetLocationScreen
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityViewModel
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherScreen
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherViewModel

@Composable
fun MainContent(
    innerPadding: PaddingValues,
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (mainViewModel.hasAlreadyChosenLocation()) Screen.Weather else Screen.FindCity,
        modifier = Modifier.padding(paddingValues = innerPadding)
    ) {
        composable<Screen.FindCity> {
            FindCityOrGetLocationScreen(
                viewModel = hiltViewModel<FindCityViewModel>(),
                navigateToWeatherScreen = {
                    navController.navigate(Screen.Weather) {
                        launchSingleTop = true
                        popUpTo(Screen.FindCity) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
            )
        }

        composable<Screen.Weather> {
            WeatherScreen(
                viewModel = hiltViewModel<WeatherViewModel>(),
                goToChooseLocation = {
                    navController.navigate(Screen.FindCity) {
                        launchSingleTop = true
                        popUpTo(Screen.Weather) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
            )
        }
    }
}