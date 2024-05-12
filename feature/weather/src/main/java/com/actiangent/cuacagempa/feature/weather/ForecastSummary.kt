package com.actiangent.cuacagempa.feature.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.actiangent.cuacagempa.core.common.datetime.text
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.icon
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.Regency
import com.actiangent.cuacagempa.core.model.asWeatherCondition

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
            .drawBehind {
                val twoColorGradient = weatherCondition.gradient(weather.dateTime.hour)
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            twoColorGradient.start,
                            twoColorGradient.end,
                        ),
                    ),
                )
            }
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                icon = weatherCondition.icon(weather.dateTime.hour),
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
                text = weather.dateTime.date.text(),
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
