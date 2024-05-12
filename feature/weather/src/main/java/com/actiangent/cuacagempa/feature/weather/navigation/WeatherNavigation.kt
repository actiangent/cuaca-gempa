package com.actiangent.cuacagempa.feature.weather.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.actiangent.cuacagempa.feature.weather.RegencyWeatherRoute
import com.actiangent.cuacagempa.feature.weather.UserRegencyForecastRoute
import com.actiangent.cuacagempa.feature.weather.UserRegencyRoute

const val WEATHER_GRAPH_ROUTE_PATTERN = "weather_graph"

const val USER_REGENCY_WEATHER_ROUTE = "user_regency_weather_route"
private const val MANAGE_USER_REGENCY_ROUTE = "manage_user_regency_route"
private const val REGENCY_WEATHER_ROUTE = "regency_weather_route"
private const val REGENCY_ID_ARG = "regencyId"

fun NavController.navigateToWeatherGraph(navOptions: NavOptions) =
    navigate(WEATHER_GRAPH_ROUTE_PATTERN, navOptions)

fun NavController.navigateToManageUserRegency() =
    navigate(MANAGE_USER_REGENCY_ROUTE)

fun NavController.navigateToRegencyWeather(regencyId: String) =
    navigate("$REGENCY_WEATHER_ROUTE/$regencyId")

fun NavGraphBuilder.weatherGraph(
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
    onManageUserRegencyClick: () -> Unit,
    onRegencyClick: (String) -> Unit,
) {
    navigation(
        route = WEATHER_GRAPH_ROUTE_PATTERN,
        startDestination = USER_REGENCY_WEATHER_ROUTE,
    ) {
        composable(
            route = USER_REGENCY_WEATHER_ROUTE
        ) {
            UserRegencyForecastRoute(
                onManageUserRegencyClick = onManageUserRegencyClick,
                onSettingClick = onSettingClick,
            )
        }
        composable(
            route = MANAGE_USER_REGENCY_ROUTE
        ) {
            UserRegencyRoute(
                onBackClick = onBackClick,
                onRegencyClick = onRegencyClick,
            )
        }
        composable(
            route = "$REGENCY_WEATHER_ROUTE/{$REGENCY_ID_ARG}",
            arguments = listOf(
                navArgument(REGENCY_ID_ARG) { type = NavType.StringType },
            ),
        ) {
            RegencyWeatherRoute(
                onBackClick = onBackClick,
            )
        }
    }
}
