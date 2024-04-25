package com.actiangent.cuacagempa.core.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId

@Composable
fun RoundedTabRow(
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Black.copy(alpha = 0.1f),
    indicatorColor: Color = Color.Black.copy(alpha = 0.3f),
    indicator: @Composable BoxScope.() -> Unit = @Composable {
        RoundedTabIndicator(color = indicatorColor)
    },
    tabs: @Composable () -> Unit
) {
    val indicatorIndex = remember { Animatable(0f) }
    LaunchedEffect(selectedIndex) {
        indicatorIndex.animateTo(
            selectedIndex.toFloat(), tween(durationMillis = 250, easing = LinearEasing)
        )
    }

    Layout(
        modifier = modifier
            .clip(RoundedTabShape)
            .background(containerColor),
        content = {
            tabs()
            Box(
                modifier = modifier
                    .layoutId("indicator"),
                content = indicator
            )
        }
    ) { measurables, constraints ->
        val indicatorMeasurables = measurables
            .first { it.layoutId == "indicator" }

        val tabPlaceables = measurables
            .filterNot { it == indicatorMeasurables }
            .map { measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = constraints.maxWidth / (measurables.size),
                        maxWidth = constraints.maxWidth / (measurables.size)
                    )
                )
            }

        val tabCount = tabPlaceables.size

        val space = constraints.maxHeight / 4
        val indicatorWidth = tabPlaceables.maxOf { it.width }
        val layoutWidth = indicatorWidth * tabCount

        val indicatorPlaceable = indicatorMeasurables.measure(
            constraints.copy(
                minWidth = indicatorWidth, maxWidth = indicatorWidth
            )
        )

        layout(layoutWidth + (space * 2), constraints.maxHeight) {
            val indicatorX = (indicatorIndex.value * indicatorWidth).toInt() +
                    ((space / 2) * (selectedIndex + 1))
            indicatorPlaceable.place(indicatorX, 0)

            var x = 0 + (space / 2)
            tabPlaceables.forEach { placeable ->
                placeable.placeRelative(x, 0)
                x += (placeable.width) + (space / 2)
            }
        }
    }
}

@Composable
fun RoundedTabIndicator(
    color: Color,
    shape: RoundedCornerShape = RoundedTabShape
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(color = color)
    )
}

@Composable
fun RoundedTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedTabShape)
            .selectable(
                selected = selected,
                onClick = onClick
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .weight(0.1f)
        )
        content()
        Spacer(
            modifier = Modifier
                .weight(0.1f)
        )
    }
}

private val RoundedTabShape = RoundedCornerShape(percent = 50)