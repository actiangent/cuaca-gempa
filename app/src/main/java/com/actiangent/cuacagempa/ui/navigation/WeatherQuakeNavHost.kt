package com.actiangent.cuacagempa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.actiangent.cuacagempa.earthquake.navigation.earthquakeScreen
import com.actiangent.cuacagempa.feature.weather.navigation.WEATHER_GRAPH_ROUTE_PATTERN
import com.actiangent.cuacagempa.feature.weather.navigation.navigateToManageUserRegency
import com.actiangent.cuacagempa.feature.weather.navigation.navigateToRegencyWeather
import com.actiangent.cuacagempa.feature.weather.navigation.weatherGraph

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
            onSettingClick = {},
            onManageUserRegencyClick = navController::navigateToManageUserRegency,
            onRegencyClick = navController::navigateToRegencyWeather
        )
        earthquakeScreen(
            onSettingClick = {}
        )
    }
}