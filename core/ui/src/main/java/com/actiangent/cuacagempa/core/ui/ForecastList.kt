package com.actiangent.cuacagempa.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.actiangent.cuacagempa.core.common.datetime.text
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.core.designsystem.icon.icon
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.asWeatherCondition
import com.actiangent.cuacagempa.core.model.temperature

@Composable
fun ForecastList(
    forecasts: List<Forecast>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedForecastIndex: Int? = null,
    onSelectedForecastIndex: ((Int) -> Unit)? = null,
) {
    val itemCount = forecasts.size

    val animatable = remember { Animatable(0f) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .graphicsLayer {
                alpha = animatable.value
            },
    ) {
        repeat(itemCount) { index ->
            val forecast = forecasts[index]
            ForecastItem(
                forecast = forecast,
                modifier = Modifier
                    .then(
                        if (enabled
                            && (selectedForecastIndex != null)
                            && onSelectedForecastIndex != null
                        )
                            Modifier
                                .clickable { onSelectedForecastIndex(index) }
                                .then(
                                    if (selectedForecastIndex == index)
                                        Modifier
                                            .background(color = Color.White.copy(alpha = 0.35f))
                                    else
                                        Modifier
                                )
                        else
                            Modifier
                    )
                    .padding(horizontal = 4.dp)
            )
            if (index < (forecasts.size - 1)) {
                Divider()
            }
        }
    }
    LaunchedEffect(Unit) {
        animatable.animateTo(
            1f,
            animationSpec = tween(durationMillis = 750, easing = LinearEasing)
        )
    }
}

@Composable
private fun ForecastItem(
    forecast: Forecast,
    modifier: Modifier = Modifier,
) {
    val weather = forecast.summary
    val weatherCondition = forecast.summary.code.asWeatherCondition()
    val date = weather.dateTime.date
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon = weatherCondition.icon(weather.dateTime.hour),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = date.text(),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "$weatherCondition",
                style = MaterialTheme.typography.titleSmall
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 8.dp),
        ) {
            Icon(
                icon = WeatherQuakeIcons.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
            )
            Text(
                text = forecast.minTemperature.temperature(),
                style = MaterialTheme.typography.titleSmall
            )
            Divider(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Text(
                text = forecast.maxTemperature.temperature(),
                style = MaterialTheme.typography.titleSmall
            )
            Icon(
                icon = WeatherQuakeIcons.ArrowDropUp,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}