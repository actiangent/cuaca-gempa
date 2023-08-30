package com.actiangent.cuacagempa.ui.navigation

import com.actiangent.cuacagempa.R
import com.actiangent.cuacagempa.core.designsystem.icon.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    WEATHER(
        selectedIcon = WeatherQuakeIcons.Cloud,
        unselectedIcon = WeatherQuakeIcons.Cloud,
        iconTextId = R.string.weather,
        titleTextId = R.string.weather,
    ),
    EARTHQUAKE(
        selectedIcon = WeatherQuakeIcons.Earthquake,
        unselectedIcon = WeatherQuakeIcons.Earthquake,
        iconTextId = R.string.earthquake,
        titleTextId = R.string.earthquake,
    )
}

val TopLevelDestination.route
    get() = this.name.lowercase()