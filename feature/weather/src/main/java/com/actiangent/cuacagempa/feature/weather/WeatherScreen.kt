package com.actiangent.cuacagempa.feature.weather

import android.util.Log
import android.widget.TextClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actiangent.cuacagempa.core.designsystem.icon.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.core.model.Weather
import kotlinx.coroutines.CoroutineScope

@Composable
fun WeatherScreen(
    requestLocation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    // without using the delegate property "by", allowing smart cast to work
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        when (uiState) {
            is WeatherUiState.Loading -> {
                // TODO - display loading state
            }
            is WeatherUiState.Success -> {
                val placeName = uiState.placeName
                val weathers = uiState.weathers
                val todayWeatherForecast = weathers[0]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextClock()
                    Text(text = placeName)
                }

                CurrentWeatherCard(weather = todayWeatherForecast)

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "6-hour forecast")
                    Text(text = "Next 2 days >")
                    Spacer(modifier = Modifier.padding(8.dp))
                    SixHourForecasts(weathers)
                }
            }
            is WeatherUiState.Error -> {
                Log.d("Weather error", "WeatherScreen: ${uiState.message}")
            }
        }
    }

}

@Composable
fun CurrentWeatherCard(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    val weatherCode = weather.code.asWeatherCode()
    val backgroundColor = weatherCode.asColor()
    val icon = weatherCode.asIcon()

    Card(
        modifier = Modifier
            .padding(16.dp)
            .heightIn(min = 384.dp, max = 512.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = backgroundColor,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterVertically
            )
        ) {
            Spacer(modifier = Modifier.heightIn(96.dp, 128.dp))
            Text(
                text = "${weather.temperature}",
                fontSize = 80.sp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row {
                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = ""
                        )
                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = ""
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = weatherCode.description)
                }
                Row {
                    Icon(
                        painter = painterResource(id = WeatherQuakeIcons.WaterDrop.id),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "${weather.humidity} %")
                }
            }
            Box(
                modifier = Modifier
                    .height(128.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color.LightGray.copy(alpha = 0.5f))
            ) {
                // TODO - temperature graph
            }
        }
    }
}

@Composable
fun SixHourForecasts(
    data: List<Weather>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(data) { weather ->
            ForecastCard(weather = weather)
        }
    }
}

@Composable
fun ForecastCard(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    val weatherCode = weather.code.asWeatherCode()
    val icon = weatherCode.asIcon()

    Card(
        backgroundColor = Color.LightGray,
        modifier = Modifier
            .width(64.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${weather.timestamp.dayOfMonth}/${weather.timestamp.monthNumber}")
            Text(text = "${weather.timestamp.hour}:${weather.timestamp.minute}")
            when (icon) {
                is Icon.ImageVectorIcon -> Icon(
                    imageVector = icon.imageVector,
                    contentDescription = ""
                )
                is Icon.DrawableResourceIcon -> Icon(
                    painter = painterResource(id = icon.id),
                    contentDescription = ""
                )
            }
            Text(text = "${weather.temperature}")
        }
    }
}

@Composable
private fun TextClock(
    modifier: Modifier = Modifier,
) {
    val textColor = Color.LightGray.toArgb()

    AndroidView(
        factory = { context ->
            TextClock(context).apply {
                setTextColor(textColor)
                format24Hour = "hh:mm:ss"
                textSize = 18F
            }
        },
        modifier = modifier,
    )
}
