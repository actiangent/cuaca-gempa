package com.actiangent.cuacagempa.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.actiangent.cuacagempa.core.designsystem.component.WeatherQuakeNavigationBar
import com.actiangent.cuacagempa.core.designsystem.component.WeatherQuakeNavigationBarItem
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.ui.navigation.TopLevelDestination
import com.actiangent.cuacagempa.ui.navigation.WeatherQuakeNavHost
import com.actiangent.cuacagempa.ui.navigation.route

@Composable
fun WeatherQuakeApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentTopLevelDestination: TopLevelDestination? by remember {
        derivedStateOf {
            when (currentBackStackEntry?.destination?.route) {
                TopLevelDestination.WEATHER.route -> TopLevelDestination.WEATHER
                TopLevelDestination.EARTHQUAKE.route -> TopLevelDestination.EARTHQUAKE
                else -> null
            }
        }
    }

    Scaffold(
        bottomBar = {
            WeatherQuakeBottomBar(
                destinations = TopLevelDestination.values().asList(),
                onNavigateToDestination = { destination ->
                    navController.navigate(destination.route)
                },
                currentDestination = currentBackStackEntry?.destination,
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            val destination = currentTopLevelDestination
            if (destination != null) {
                WeatherQuakeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = destination.titleTextId),
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f)
                        )
                    },
                    navigateToSettings = { }
                )
            }

            WeatherQuakeNavHost(
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherQuakeTopAppBar(
    title: @Composable () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = title,
        navigationIcon = {},
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    icon = WeatherQuakeIcons.SettingsFilled,
                    contentDescription = "settings",
                )
            }
        },
        modifier = modifier,
    )
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
                        color = labelTint,
                        style = MaterialTheme.typography.labelLarge,
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