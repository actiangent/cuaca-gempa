package com.actiangent.cuacagempa.earthquake.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.actiangent.cuacagempa.earthquake.EarthquakeRoute

const val EARTHQUAKE_ROUTE = "earthquake_route"

fun NavController.navigateToEarthquake(navOptions: NavOptions) =
    navigate(EARTHQUAKE_ROUTE, navOptions)

fun NavGraphBuilder.earthquakeScreen(
    onSettingClick: () -> Unit,
) {
    composable(route = EARTHQUAKE_ROUTE) {
        EarthquakeRoute(
            onSettingClick = onSettingClick,
        )
    }
}