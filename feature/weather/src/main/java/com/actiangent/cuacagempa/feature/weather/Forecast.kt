package com.actiangent.cuacagempa.feature.weather

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.core.designsystem.icon.icon
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.Regency
import com.actiangent.cuacagempa.core.model.asWeatherCondition
import com.actiangent.cuacagempa.core.model.temperature

@Composable
internal fun ForecastSummary(
    regency: Regency,
    forecast: Forecast,
    modifier: Modifier = Modifier
) {
    val weather = forecast.summary
    val weatherCondition = forecast.summary.code.asWeatherCondition()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = weatherCondition.brush(weather.timestamp.hour)
            )
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                icon = weatherCondition.icon(weather.timestamp.hour),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$weatherCondition",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = weather.timestamp.date.display(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = regency.name,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
internal fun ForecastList(
    forecasts: List<Forecast>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        repeat(forecasts.size) { index ->
            val forecast = forecasts[index]
            ForecastItem(
                forecast = forecast,
                modifier = modifier
            )
            if (index < (forecasts.size - 1)) {
                Divider()
            }
        }
    }
}

@Composable
internal fun ForecastItem(
    forecast: Forecast,
    modifier: Modifier = Modifier,
) {
    val weather = forecast.summary
    val weatherCondition = forecast.summary.code.asWeatherCondition()
    val date = weather.timestamp.date
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
                icon = weatherCondition.icon(weather.timestamp.hour),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = date.display(),
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