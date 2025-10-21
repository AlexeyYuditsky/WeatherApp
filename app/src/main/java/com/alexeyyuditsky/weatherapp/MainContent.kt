package com.alexeyyuditsky.weatherapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexeyyuditsky.weatherapp.core.presentation.Routes
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityOrGetLocationScreen
import com.alexeyyuditsky.weatherapp.findCity.presentation.FindCityViewModel
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherScreen
import com.alexeyyuditsky.weatherapp.weather.presentation.WeatherViewModel

@Composable
fun MainContent(innerPadding: PaddingValues) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (mainViewModel.hasAlreadyChosenLocation()) Routes.WEATHER else Routes.FIND_CITY,
        modifier = Modifier.padding(paddingValues = innerPadding)
    ) {
        composable(route = Routes.FIND_CITY) {
            FindCityOrGetLocationScreen(
                viewModel = hiltViewModel<FindCityViewModel>(),
                navigateToWeatherScreen = {
                    navController.navigate(Routes.WEATHER) {
                        launchSingleTop = true
                        popUpTo(Routes.FIND_CITY) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
            )
        }

        composable(route = Routes.WEATHER) {
            WeatherScreen(
                viewModel = hiltViewModel<WeatherViewModel>(),
                goToChooseLocation = {
                    navController.navigate(Routes.FIND_CITY) {
                        launchSingleTop = true
                        popUpTo(Routes.WEATHER) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
            )
        }
    }
}