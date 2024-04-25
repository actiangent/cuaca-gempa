package com.actiangent.cuacagempa.feature.weather.weather

import android.util.LayoutDirection
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actiangent.cuacagempa.core.designsystem.component.RoundedTab
import com.actiangent.cuacagempa.core.designsystem.component.RoundedTabRow
import com.actiangent.cuacagempa.core.designsystem.component.TextIcon
import com.actiangent.cuacagempa.core.designsystem.icon.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.core.model.WeatherCondition
import com.actiangent.cuacagempa.core.model.Weathers
import com.actiangent.cuacagempa.core.model.asWeatherCondition
import com.actiangent.cuacagempa.core.model.asWeathers
import com.actiangent.cuacagempa.core.model.temperature
import com.actiangent.cuacagempa.feature.weather.WeatherUiState
import com.actiangent.cuacagempa.feature.weather.brush
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherScreen(
    navigateToSearchRegency: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val userRegencies by viewModel.userRegencies.collectAsStateWithLifecycle()
    // without using the delegate property "by", allowing smart cast to work
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val pagerState = rememberPagerState()
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setSelectedUserRegencyIndex(pagerState.currentPage)
    }
    HorizontalPager(
        pageCount = userRegencies.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxHeight()
    ) { page ->
        val isRtl =
            LocalContext.current.resources.configuration.layoutDirection == LayoutDirection.RTL
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                    alpha = lerp(
                        start = 0f,
                        stop = 1f,
                        fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                    )

                    val fraction =
                        if (pagerState.currentPage != page) -pageOffset else 1f - pageOffset
                    translationX = lerp(
                        start = size.width,
                        stop = 0f,
                        fraction = if (isRtl) 2f - fraction else fraction,
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is WeatherUiState.Loading -> {

                }

                is WeatherUiState.Success -> {
                    WeatherCard(
                        navigateToSearchRegency = navigateToSearchRegency,
                        weathers = uiState.data.forecasts.flatMap { it.weathers }.asWeathers(),
                        selectedWeatherFilterIndex = 0,
                        onSelectedWeatherFilterIndex = { },
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }

                is WeatherUiState.Error -> {

                }
            }
        }
    }

//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(8.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        when (uiState) {
//            is WeatherUiState.Loading -> {
//                // TODO - display loading state
//            }
//
//            is WeatherUiState.Success -> {
//                WeatherCard(
//                    navigateToSearchDistrict = navigateToSearchDistrict,
//                    weathers = uiState.data.forecasts.flatMap { it.weathers }.asWeathers(),
//                    selectedWeatherFilterIndex = selectedWeatherFilterIndex,
//                    onSelectedWeatherFilterIndex = { index -> selectedWeatherFilterIndex = index },
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                )
//                Spacer(
//                    modifier = modifier.padding(8.dp)
//                )
//                Box(
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "Credits: BMKG",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            }
//
//            is WeatherUiState.Error -> {
//                Log.d("Weather error", "WeatherScreen: ${uiState.message}")
//            }
//        }
//    }
}

@Composable
fun WeatherCard(
    navigateToSearchRegency: () -> Unit,
    weathers: Weathers,
    selectedWeatherFilterIndex: Int,
    onSelectedWeatherFilterIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        var selectedWeatherIndex by remember { mutableStateOf(0) }
        val weather = weathers.weathers[selectedWeatherIndex]
        val weatherCode = weather.code.asWeatherCondition()

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(
                    brush = weatherCode.brush(weather.timestamp.hour)
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                IconButton(onClick = { navigateToSearchRegency() }) {
                    Icon(
                        icon = Icon.ImageVectorIcon(imageVector = Icons.Rounded.Add),
                        contentDescription = ""
                    )
                }
            }
            Spacer(
                modifier = modifier
                    .padding(vertical = 16.dp)
            )
            WeatherInfo(
                condition = weatherCode,
                temperature = weather.temperature,
                minTemperature = weathers.minTemperature ?: 0.0,
                maxTemperature = weathers.maxTemperature ?: 0.0,
                humidity = weather.humidity,
                cardinal = weather.cardinal,
                knot = weather.knot
            )
            Spacer(
                modifier = modifier
                    .padding(vertical = 24.dp)
            )
            WeatherFilterTab(
                titles = listOf("6 Hour", "3 Days"),
                selectedIndex = selectedWeatherFilterIndex,
                onClick = onSelectedWeatherFilterIndex
            )
            Spacer(
                modifier = modifier
                    .padding(vertical = 16.dp)
            )
            WeatherGraph(
                weathers = weathers.weathers,
                selectedIndex = selectedWeatherIndex,
                modifier = modifier
            ) { index ->
                selectedWeatherIndex = index
            }
        }
    }
}

@Composable
fun WeatherInfo(
    condition: WeatherCondition,
    temperature: Double,
    minTemperature: Double,
    maxTemperature: Double,
    humidity: Int,
    cardinal: String,
    knot: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Jakarta",
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(
            modifier = modifier
                .padding(8.dp)
        )
        WeatherInfoTemperature(
            temperature = temperature,
            minTemperature = minTemperature,
            maxTemperature = maxTemperature
        )
        Spacer(
            modifier = modifier
                .padding(8.dp)
        )
        Text(
            text = condition.toString(),
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(
            modifier = modifier
                .padding(8.dp)
        )
        WeatherInfoHumidityAndWind(
            humidity = humidity,
            windDirection = cardinal,
            windSpeed = "$knot kn"
        )
    }
}

@Composable
fun ColumnScope.WeatherInfoTemperature(
    temperature: Double,
    minTemperature: Double,
    maxTemperature: Double,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .align(Alignment.CenterHorizontally)
    ) {
        TextIcon(
            text = {
                Text(
                    text = minTemperature.temperature(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            leadingIcon = {
                Icon(
                    icon = WeatherQuakeIcons.ArrowDropDown,
                    contentDescription = ""
                )
            }
        )
        Spacer(
            modifier = modifier
                .padding(12.dp)
        )
        Text(
            text = temperature.temperature(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(
            modifier = modifier
                .padding(12.dp)
        )
        TextIcon(
            text = {
                Text(
                    text = maxTemperature.temperature(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            leadingIcon = {
                Icon(
                    icon = WeatherQuakeIcons.ArrowDropUp,
                    contentDescription = ""
                )
            },
        )
    }
}

@Composable
fun ColumnScope.WeatherInfoHumidityAndWind(
    humidity: Int,
    windDirection: String,
    windSpeed: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .align(Alignment.CenterHorizontally)
    ) {
        TextIcon(
            text = {
                Text(
                    text = "${humidity}%",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            leadingIcon = {
                Icon(
                    icon = WeatherQuakeIcons.HumidityPercentage,
                    contentDescription = ""
                )
            },
        )
        Spacer(
            modifier = modifier
                .padding(16.dp)
        )
        TextIcon(
            text = {
                Text(
                    text = "${windDirection}, $windSpeed",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            leadingIcon = {
                Icon(
                    icon = WeatherQuakeIcons.Air,
                    contentDescription = ""
                )
            }
        )
    }
}

@Composable
fun WeatherFilterTab(
    titles: List<String>,
    selectedIndex: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    RoundedTabRow(
        selectedIndex = selectedIndex,
        modifier = modifier
            .height(WeatherTabRowHeight)
            .padding(vertical = (WeatherTabRowHeight - WeatherTabHeight) / 2)
    ) {
        titles.forEachIndexed { index, title ->
            RoundedTab(
                selected = (index == selectedIndex),
                onClick = { onClick(index) },
                modifier = Modifier
                    .height(WeatherTabHeight)
                    .padding(vertical = (WeatherTabRowHeight - WeatherTabHeight) / 2)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}


private val WeatherTabRowHeight = 40.dp
private val WeatherTabHeight = 32.dp


/*
@Composable
private fun TextClock(
    modifier: Modifier = Modifier,
) {
    val textColor = MaterialTheme.colorScheme.onPrimary
        .toArgb()

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
 */
