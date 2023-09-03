package com.actiangent.cuacagempa.core.designsystem.component.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

// Cubic bezier line graph
@OptIn(ExperimentalTextApi::class)
@Composable
fun LineGraph(
    entries: List<GraphEntry>,
    modifier: Modifier = Modifier,
    point: (DrawScope.(x: Float, y: Float) -> Unit)? = null,
    brush: Brush = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.1f),
            Color.Transparent,
        ),
        tileMode = TileMode.Clamp
    ),
    style: DrawStyle = Fill,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    animatable: Animatable<Float, AnimationVector1D> = remember { Animatable(0f) },
    animatableTargetValue: Float = 1f,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 1000, easing = LinearEasing),
) {
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = animatableTargetValue, animationSpec = animationSpec
        )
    }

    Canvas(
        modifier = modifier
    ) {
        // clipRect for left-to-right visibility animation
        clipRect(
            left = 0f,
            top = 0f,
            right = size.width * animatable.value,
            bottom = size.height,
        ) {
            val values = entries.map { graphEntry -> graphEntry.value.toFloat() }
            val minValue = values.min()
            val maxValue = values.max()

            val space = size.width / values.size
            val offsets = values.mapIndexed { index, value -> // index start at 0
                Offset(
                    x = (space * index) + (space / 2),
                    y = (1 - (value - minValue) / (maxValue - minValue)) * (size.height * 0.3f)
                            + (size.height * 0.2f)
                )
            }

            val path = Path().apply {
                offsets.forEachIndexed { index, offset ->
                    if (index == offsets.indices.first) {
                        moveTo(
                            x = offset.x,
                            y = offset.y
                        )
                    } else {
                        cubicTo(
                            x1 = (offsets[index - 1].x + offset.x) / 2f,
                            y1 = offsets[index - 1].y,
                            x2 = (offsets[index - 1].x + offset.x) / 2f,
                            y2 = offset.y,
                            x3 = offset.x,
                            y3 = offset.y
                        )
                    }

                    val textLayoutResult = textMeasurer.measure(entries[index].label, textStyle)
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = offset.copy(
                            x = offset.x - (textLayoutResult.size.width / 2),
                            y = offset.y - (size.height * 0.15f)
                        )
                    )
                }

                lineTo(size.width, offsets.last().y)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                lineTo(0f, offsets.first().y)
                lineTo(offsets.first().x, offsets.first().y)
            }

            drawPath(
                path = path,
                brush = brush,
                style = style
            )

            offsets.forEach { offset ->
                if (point == null) {
                    drawCircle(
                        color = Color.Gray.copy(alpha = animatable.value),
                        center = Offset(
                            x = offset.x,
                            y = offset.y
                        ),
                        radius = 4.dp.toPx()
                    )
                } else {
                    point(offset.x, offset.y)
                }

                drawLine(
                    color = Color.Black.copy(alpha = 0.7f),
                    start = offset.copy(y = size.height),
                    end = offset,
                    pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 20f)),
                )
            }
        }
    }
}
