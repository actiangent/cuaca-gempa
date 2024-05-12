package com.actiangent.cuacagempa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.actiangent.cuacagempa.earthquake.navigation.earthquakeScreen
import com.actiangent.cuacagempa.feature.weather.navigation.WEATHER_GRAPH_ROUTE_PATTERN
import com.actiangent.cuacagempa.feature.weather.navigation.navigateToManageUserRegency
import com.actiangent.cuacagempa.feature.weather.navigation.navigateToRegencyWeather
import com.actiangent.cuacagempa.feature.weather.navigation.weatherGraph
import com.actiangent.cuacagempa.ui.SETTINGS_ROUTE
import com.actiangent.cuacagempa.ui.SettingsScreen

@Composable
fun WeatherQuakeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = WEATHER_GRAPH_ROUTE_PATTERN,
        modifier = modifier,
    ) {
        weatherGraph(
            onBackClick = navController::popBackStack,
            onSettingClick = { navController.navigate(SETTINGS_ROUTE) },
            onManageUserRegencyClick = navController::navigateToManageUserRegency,
            onRegencyClick = navController::navigateToRegencyWeather
        )
        earthquakeScreen(
            onSettingClick = { navController.navigate(SETTINGS_ROUTE) }
        )
        composable(route = SETTINGS_ROUTE) {
            SettingsScreen(
                onBackClick = navController::popBackStack,
            )
        }
    }
}