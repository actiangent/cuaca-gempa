package com.actiangent.cuacagempa.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun WeatherQuakeNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) = CompositionLocalProvider(LocalRippleTheme provides NavigationBarNoRippleTheme) {
    NavigationBar(
        modifier = modifier,
        content = content
    )
}

@Composable
fun RowScope.WeatherQuakeNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    alwaysShowLabel: Boolean = false,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        modifier = modifier,
        interactionSource = interactionSource
    )
}

private object NavigationBarNoRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}
