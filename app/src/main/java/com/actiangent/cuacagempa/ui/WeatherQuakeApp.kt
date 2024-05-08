package com.actiangent.cuacagempa.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.actiangent.cuacagempa.core.designsystem.component.WeatherQuakeNavigationBar
import com.actiangent.cuacagempa.core.designsystem.component.WeatherQuakeNavigationBarItem
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.feature.weather.navigation.navigateToWeatherGraph
import com.actiangent.cuacagempa.ui.navigation.TopLevelDestination
import com.actiangent.cuacagempa.ui.navigation.WeatherQuakeNavHost

@Composable
fun WeatherQuakeApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (
                currentDestination.isCurrentDestinationTopLevelDestination(
                    TopLevelDestination.values().toList()
                )
            ) {
                WeatherQuakeBottomBar(
                    destinations = TopLevelDestination.values().asList(),
                    onNavigateToDestination = { destination ->
                        val topLevelNavOptions = navOptions {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }

                        when (destination) {
                            TopLevelDestination.WEATHER ->
                                navController.navigateToWeatherGraph(topLevelNavOptions)

                            TopLevelDestination.EARTHQUAKE ->
                                navController.navigate("earthquake_route", topLevelNavOptions)
                        }
                    },
                    currentDestination = currentDestination,
                )
            }
        },
    ) { paddingValues ->
        WeatherQuakeNavHost(
            navController = navController,
            modifier = modifier
                .padding(paddingValues),
        )
    }
}

@Composable
private fun WeatherQuakeBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    WeatherQuakeNavigationBar(
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            val iconTint by animateColorAsState(
                if (selected || isPressed) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            val labelTint by animateColorAsState(
                if (selected || isPressed) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            WeatherQuakeNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        icon = destination.unselectedIcon,
                        contentDescription = null,
                        tint = iconTint,
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.textId),
                        color = labelTint,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                selectedIcon = {
                    Icon(
                        icon = destination.selectedIcon,
                        contentDescription = null,
                        tint = iconTint,
                    )
                },
                interactionSource = interactionSource,
                alwaysShowLabel = false,
            )
        }
    }
}

private fun NavDestination?.isCurrentDestinationTopLevelDestination(destinations: List<TopLevelDestination>) =
    destinations.any {
        this?.route?.contains(it.name, true) ?: false
    }

// taken from android/nowinandroid
private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@Preview
@Composable
fun WeatherQuakeAppPreview() {
    WeatherQuakeApp()
}