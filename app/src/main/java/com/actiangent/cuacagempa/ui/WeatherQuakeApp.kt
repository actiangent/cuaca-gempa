package com.actiangent.cuacagempa.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.actiangent.cuacagempa.ui.navigation.WeatherQuakeNavHost

enum class WeatherQuakeDestination(
    val label: String
) {
    WEATHER("Cuaca"),
    EARTHQUAKE("Gempa")
}

@Composable
fun WeatherQuakeApp(
    requestLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentDestination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = modifier
            ) {
                NavigationBarItem(
                    selected = currentDestination?.contains(
                        WeatherQuakeDestination.WEATHER.label,
                        true
                    ) ?: false,
                    onClick = { navController.navigate(WeatherQuakeDestination.WEATHER.label) },
                    modifier = modifier,
                    icon = {},
                    label = { Text(text = WeatherQuakeDestination.WEATHER.label) }
                )
                NavigationBarItem(
                    selected = currentDestination?.contains(
                        WeatherQuakeDestination.EARTHQUAKE.label,
                        true
                    ) ?: false,
                    onClick = { navController.navigate(WeatherQuakeDestination.EARTHQUAKE.label) },
                    modifier = modifier,
                    icon = {},
                    label = { Text(text = WeatherQuakeDestination.EARTHQUAKE.label) }
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            WeatherQuakeNavHost(
                requestLocation = requestLocation,
                navController = navController
            )
        }
    }
}

@Preview
@Composable
fun WeatherQuakeAppPreview() {
    WeatherQuakeApp(requestLocation = {})
}