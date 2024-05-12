package com.actiangent.cuacagempa.feature.weather

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.actiangent.cuacagempa.core.designsystem.component.LineGraph
import com.actiangent.cuacagempa.core.designsystem.model.GraphEntry
import com.actiangent.cuacagempa.core.model.Weather
import com.actiangent.cuacagempa.core.model.temperature
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun WeatherGraph(
    weathers: List<Weather>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    onSelectedIndex: (Int) -> Unit,
) {
    val weatherEntries = weathers.map { weather ->
        val temperature = weather.temperature
        GraphEntry(
            value = temperature,
            label = temperature.temperature()
        )
    }

    Box(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(top = 16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val itemCount = weatherEntries.size
        val (itemWidthPx, itemHeightPx) = with(LocalDensity.current) {
            WeatherGraphItemWidth.toPx() to WeatherGraphHeight.toPx()
        }

        val textMeasurer = rememberTextMeasurer()
        val animatable = remember { Animatable(0f) }
        val indicatorX = remember { Animatable(0f) }
        LaunchedEffect(selectedIndex) {
            indicatorX.animateTo(targetValue = itemWidthPx * selectedIndex)
        }

        val textColor = Color.Black

        LineGraph(
            entries = weatherEntries,
            modifier = Modifier
                .width(WeatherGraphItemWidth * itemCount)
                .height(WeatherGraphHeight)
                .drawWithContent {
                    drawContent()

                    val textY = size.height - (size.height * 0.25f)
                    weathers.forEachIndexed { index, weather ->
                        val dateTime = weather.dateTime
                        val hourMinuteTextLayoutResult = textMeasurer.measure(
                            text = "${dateTime.time}"
                        )

                        val dayOfWeekToDraw = dateTime.dayOfWeek
                            .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        val dayOfWeekTextLayoutResult = textMeasurer.measure(
                            text = dayOfWeekToDraw
                        )

                        val textX = (itemWidthPx * index) + (itemWidthPx / 2)
                        drawText(
                            textLayoutResult = hourMinuteTextLayoutResult,
                            topLeft = Offset(
                                x = textX - (hourMinuteTextLayoutResult.size.width / 2),
                                y = textY
                            ),
                            color = textColor.copy(alpha = animatable.value)
                        )
                        drawText(
                            textLayoutResult = dayOfWeekTextLayoutResult,
                            topLeft = Offset(
                                x = textX - (dayOfWeekTextLayoutResult.size.width / 2),
                                y = textY + hourMinuteTextLayoutResult.size.height
                            ),
                            color = textColor.copy(alpha = animatable.value)
                        )
                    }

                    val indicatorStart = 0f + indicatorX.value
                    val indicatorEnd = indicatorStart + itemWidthPx

                    val indicatorPath = Path().apply {
                        val cornerRadius = CornerRadius(
                            x = WeatherGraphIndicatorCornerRadiusValue.toPx(),
                            y = WeatherGraphIndicatorCornerRadiusValue.toPx()
                        )
                        addRoundRect(
                            RoundRect(
                                rect = Rect(
                                    offset = Offset(x = indicatorStart, y = 0f),
                                    size = Size(
                                        width = itemWidthPx,
                                        height = itemHeightPx
                                    ),
                                ),
                                topLeft = cornerRadius,
                                topRight = cornerRadius
                            )
                        )

                        close()
                    }
                    val indicatorBrush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.15f * animatable.value)
                        )
                    )
                    drawPath(
                        path = indicatorPath,
                        brush = indicatorBrush,
                        style = Fill
                    )

                    val indicatorLineY = size.height - (1.dp.toPx())
                    drawLine(
                        color = Color.White.copy(alpha = animatable.value),
                        start = Offset(x = indicatorStart, y = indicatorLineY),
                        end = Offset(x = indicatorEnd, y = indicatorLineY),
                        strokeWidth = 8f
                    )
                }
                .pointerInput(itemCount) {
                    detectTapGestures { offset ->
                        val index = calculateItemIndex(itemCount - 1, offset)
                        onSelectedIndex(index)
                    }
                },
        )

        LaunchedEffect(Unit) {
            animatable.animateTo(
                1f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
        }
    }
}

private fun PointerInputScope.calculateItemIndex(
    lastIndex: Int,
    offset: Offset,
): Int {
    val itemWidth = WeatherGraphItemWidth.toPx()
    return (((size.width - offset.x) / itemWidth).toInt() - lastIndex).absoluteValue
}

private val WeatherGraphHeight = 240.dp
private val WeatherGraphItemWidth = 56.dp
private val WeatherGraphIndicatorCornerRadiusValue = 4.dp
