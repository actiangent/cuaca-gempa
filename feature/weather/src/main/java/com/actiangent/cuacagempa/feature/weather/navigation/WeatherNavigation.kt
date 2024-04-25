package com.actiangent.cuacagempa.feature.weather.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.actiangent.cuacagempa.feature.weather.RegencyWeatherScreen
import com.actiangent.cuacagempa.feature.weather.SearchRegencyScreen
import com.actiangent.cuacagempa.feature.weather.weather.WeatherScreen

const val WEATHER_GRAPH = "weather_graph"

private const val USER_REGENCY_WEATHER_ROUTE = "user_regency_weather_route"
private const val SEARCH_REGENCY_ROUTE = "search_regency_route"
private const val REGENCY_WEATHER_ROUTE = "regency_weather_route"
private const val REGENCY_WEATHER_ARG = "regencyId"

fun NavGraphBuilder.weatherGraph(navController: NavController) {
    navigation(
        startDestination = USER_REGENCY_WEATHER_ROUTE,
        route = WEATHER_GRAPH
    ) {
        composable(
            route = USER_REGENCY_WEATHER_ROUTE
        ) {
            WeatherScreen(
                navigateToSearchRegency = navController::navigateToSearchRegency
            )
        }
        composable(
            route = SEARCH_REGENCY_ROUTE
        ) {
            SearchRegencyScreen(
                navigateUp = navController::navigateUp,
                navigateToRegencyWeather = navController::navigateToRegencyWeather
            )
        }
        composable(
            route = "$REGENCY_WEATHER_ROUTE/{$REGENCY_WEATHER_ARG}",
            arguments = listOf(
                navArgument(REGENCY_WEATHER_ARG) { type = NavType.StringType },
            ),
        ) {
            RegencyWeatherScreen(
                navigateUp = navController::navigateUp
            )
        }
    }
}

fun NavController.navigateToWeatherGraph(navOptions: NavOptions) =
    navigate(USER_REGENCY_WEATHER_ROUTE, navOptions)

internal fun NavController.navigateToSearchRegency() = navigate(SEARCH_REGENCY_ROUTE)

internal fun NavController.navigateToRegencyWeather(regencyId: String) {
    navigate("$REGENCY_WEATHER_ROUTE/$regencyId")
}