package com.actiangent.cuacagempa.ui.navigation

import com.actiangent.cuacagempa.R
import com.actiangent.cuacagempa.core.designsystem.icon.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val textId: Int,
) {
    WEATHER(
        selectedIcon = WeatherQuakeIcons.Cloud,
        unselectedIcon = WeatherQuakeIcons.Cloud,
        textId = R.string.weather,
    ),
    EARTHQUAKE(
        selectedIcon = WeatherQuakeIcons.Earthquake,
        unselectedIcon = WeatherQuakeIcons.Earthquake,
        textId = R.string.earthquake,
    )
}