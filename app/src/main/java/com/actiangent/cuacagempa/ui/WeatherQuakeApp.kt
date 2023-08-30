package com.actiangent.cuacagempa.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.actiangent.cuacagempa.core.designsystem.component.WeatherQuakeNavigationBar
import com.actiangent.cuacagempa.core.designsystem.component.WeatherQuakeNavigationBarItem
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.ui.navigation.TopLevelDestination
import com.actiangent.cuacagempa.ui.navigation.WeatherQuakeNavHost
import com.actiangent.cuacagempa.ui.navigation.route

@Composable
fun WeatherQuakeApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            WeatherQuakeBottomBar(
                destinations = TopLevelDestination.values().asList(),
                onNavigateToDestination = { destination ->
                    navController.navigate(destination.route)
                },
                currentDestination = currentDestination?.destination,
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            WeatherQuakeNavHost(
                navController = navController
            )
        }
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
                onSelected = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        icon = destination.unselectedIcon,
                        contentDescription = "",
                        tint = iconTint,
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.iconTextId),
                        color = labelTint
                    )
                },
                interactionSource = interactionSource,
                selectedIcon = {
                    Icon(
                        icon = destination.selectedIcon,
                        contentDescription = "",
                        tint = iconTint,
                    )
                },
                alwaysShowLabel = false,
            )
        }
    }
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