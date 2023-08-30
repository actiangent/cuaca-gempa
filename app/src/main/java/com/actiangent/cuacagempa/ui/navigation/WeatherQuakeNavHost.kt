package com.actiangent.cuacagempa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.actiangent.cuacagempa.feature.weather.WeatherScreen

@Composable
fun WeatherQuakeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = TopLevelDestination.WEATHER.route,
        modifier = modifier,
    ) {
        composable(TopLevelDestination.WEATHER.route) {
            WeatherScreen()
        }
        composable(TopLevelDestination.EARTHQUAKE.route) {
            // EarthquakeScreen()
        }
    }
}