package com.actiangent.cuacagempa.feature.weather

import android.util.LayoutDirection
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.core.model.Forecast
import com.actiangent.cuacagempa.core.model.Weather
import com.actiangent.cuacagempa.core.model.WeatherCondition
import com.actiangent.cuacagempa.core.model.asWeatherCondition
import com.actiangent.cuacagempa.core.model.firstByDate
import com.actiangent.cuacagempa.core.model.temperature
import com.actiangent.cuacagempa.core.ui.ForecastList
import kotlin.math.absoluteValue

@Composable
fun UserRegencyForecastRoute(
    onManageUserRegencyClick: () -> Unit,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserRegencyForecastViewModel = hiltViewModel(),
) {
    val userRegenciesUiState by viewModel.userRegenciesUiState.collectAsStateWithLifecycle()
    val forecastsUiState by viewModel.forecastsUiState.collectAsStateWithLifecycle()

    val selectedRegencyIdIndex by viewModel.selectedUserRegencyIndex.collectAsStateWithLifecycle()

    UserRegencyForecastScreen(
        selectedRegencyIdIndex = selectedRegencyIdIndex,
        onSelectedRegencyIdIndex = viewModel::setSelectedUserRegencyIndex,
        userRegenciesUiState = userRegenciesUiState,
        forecastsUiState = forecastsUiState,
        onManageUserRegencyClick = onManageUserRegencyClick,
        onSettingClick = onSettingClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UserRegencyForecastScreen(
    selectedRegencyIdIndex: Int,
    onSelectedRegencyIdIndex: (Int) -> Unit,
    userRegenciesUiState: UserRegenciesUiState,
    forecastsUiState: UserRegencyForecastsUiState,
    onManageUserRegencyClick: () -> Unit,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState.currentPage) {
        onSelectedRegencyIdIndex(pagerState.currentPage)
    }

    var currentTwoColorGradient by remember {
        mutableStateOf(TwoColorGradient(start = Color.Gray, end = Color.DarkGray))
    }
    val animatedGradientColorStart by animateColorAsState(
        targetValue = currentTwoColorGradient.start,
        animationSpec = tween(1500),
    )
    val animatedGradientColorEnd by animateColorAsState(
        targetValue = currentTwoColorGradient.end,
        animationSpec = tween(1500),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            animatedGradientColorStart,
                            animatedGradientColorEnd,
                        ),
                    ),
                )
            }
    ) {
        var pageCount by remember { mutableStateOf(0) }
        TopAppBar(
            title = {
                Box(
                    modifier = modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    RegencyForecastPagerIndicators(
                        currentIndex = pagerState.currentPage,
                        indicatorCount = pageCount,
                        isLoading = forecastsUiState is UserRegencyForecastsUiState.Loading,
                        modifier = modifier

                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onManageUserRegencyClick,
                ) {
                    Icon(
                        icon = WeatherQuakeIcons.Add,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = onSettingClick,
                ) {
                    Icon(
                        icon = WeatherQuakeIcons.SettingsFilled,
                        contentDescription = null,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        when (userRegenciesUiState) {
            UserRegenciesUiState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UserRegenciesUiState.Success -> {
                RegencyForecastPager(
                    pageCount = userRegenciesUiState.regencies.size,
                    pagerState = pagerState,
                    modifier = modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .padding(vertical = 16.dp)
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState()),
                    ) {
                        val regency = userRegenciesUiState.regencies[selectedRegencyIdIndex]
                        Text(
                            text = regency.name,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.titleMedium
                        )
                        when (forecastsUiState) {
                            UserRegencyForecastsUiState.Loading ->
                                EmptyRegencyForecastInfoContent(
                                    modifier = modifier,
                                )

                            is UserRegencyForecastsUiState.Success -> {
                                var selectedWeatherIndex by remember { mutableStateOf(0) }
                                var selectedForecastIndex by remember { mutableStateOf(0) }

                                val forecasts = forecastsUiState.forecasts
                                val weathers = forecasts.flatMap(Forecast::weathers)

                                PopulatedForecastInfoContent(
                                    selectedWeatherIndex = selectedWeatherIndex,
                                    onSelectedWeatherIndex = { selectedWeatherIndex = it },
                                    selectedForecastIndex = selectedForecastIndex,
                                    onSelectedForecastIndex = { selectedForecastIndex = it },
                                    forecasts = forecasts,
                                    weathers = weathers,
                                    modifier = modifier,
                                )

                                LaunchedEffect(selectedWeatherIndex) {
                                    val selectedWeather = weathers[selectedWeatherIndex]
                                    currentTwoColorGradient = selectedWeather.code
                                        .asWeatherCondition()
                                        .gradient(selectedWeather.timestamp.hour)
                                }
                            }

                            is UserRegencyForecastsUiState.Error ->
                                ErrorRegencyForecastInfoContent(
                                    message = forecastsUiState.message,
                                    modifier = modifier,
                                )
                        }
                    }
                }

                LaunchedEffect(userRegenciesUiState) {
                    pageCount = userRegenciesUiState.regencies.size
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RegencyForecastPager(
    pageCount: Int,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    pageContent: @Composable () -> Unit,
) {
    HorizontalPager(
        pageCount = pageCount,
        state = pagerState,
        modifier = modifier
    ) { page ->
        val isRtl =
            LocalContext.current.resources.configuration.layoutDirection == LayoutDirection.RTL
        Box(
            modifier = Modifier
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
            contentAlignment = Alignment.TopCenter
        ) {
            pageContent()
        }
    }
}

@Composable
private fun RegencyForecastPagerIndicators(
    currentIndex: Int,
    indicatorCount: Int,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val delay = 500
    val infiniteTransition = rememberInfiniteTransition()

    val alphas = List(indicatorCount) {
        infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 0.1f,
            animationSpec = infiniteRepeatable(animation = keyframes {
                durationMillis = indicatorCount * delay
                0.1f at delay with LinearEasing
                1f at delay + delay with LinearEasing
                0.1f at delay + (indicatorCount * delay)
            })
        )
    }.map(State<Float>::value)

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        repeat(indicatorCount) { index ->
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .drawBehind {
                        drawCircle(
                            color = Color.Gray.copy(
                                alpha = if (isLoading) {
                                    alphas[currentIndex]
                                } else if (currentIndex == index) {
                                    0.7f
                                } else {
                                    0.3f
                                }
                            )
                        )
                    },
            )
        }
    }
}

@Composable
private fun EmptyRegencyForecastInfoContent(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
    ) {

    }
}

@Composable
private fun PopulatedForecastInfoContent(
    selectedWeatherIndex: Int,
    onSelectedWeatherIndex: (Int) -> Unit,
    selectedForecastIndex: Int,
    onSelectedForecastIndex: (Int) -> Unit,
    forecasts: List<Forecast>,
    weathers: List<Weather>,
    modifier: Modifier = Modifier,
) {
    val selectedWeather = weathers[selectedWeatherIndex]
    val selectedForecast = forecasts[selectedForecastIndex]

    var selectedForecastFilterIndex by remember { mutableStateOf(0) }

    val weatherCondition: WeatherCondition = selectedWeather.code.asWeatherCondition()

    LaunchedEffect(selectedWeatherIndex) {
        onSelectedForecastIndex(
            forecasts.indexOf(
                forecasts.firstByDate(selectedWeather.timestamp.date)
            )
        )
    }

    LaunchedEffect(selectedForecastIndex) {
        if (selectedForecastFilterIndex == ForecastFilter.DAILY.ordinal) {
            onSelectedWeatherIndex(
                weathers.indexOf(
                    weathers.firstByDate(selectedForecast.summary.timestamp.date)
                )
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        WeatherInfo(
            condition = weatherCondition,
            temperature = selectedWeather.temperature,
            minTemperature = selectedForecast.minTemperature,
            maxTemperature = selectedForecast.maxTemperature,
            humidity = selectedWeather.humidity,
            cardinal = selectedWeather.cardinal,
            knot = selectedWeather.knot
        )
        Spacer(
            modifier = modifier
                .padding(top = 24.dp)
        )
        ForecastFilterTab(
            titles = ForecastFilter.titles(),
            selectedIndex = selectedForecastFilterIndex,
            onClick = { selectedForecastFilterIndex = it },
        )
        Spacer(
            modifier = modifier
                .padding(vertical = 16.dp)
        )
        Box(
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            if (selectedForecastFilterIndex == ForecastFilter.SIXHOURS.ordinal) {
                WeatherGraph(
                    weathers = weathers,
                    selectedIndex = selectedWeatherIndex,
                    onSelectedIndex = onSelectedWeatherIndex,
                    modifier = modifier,
                )
            } else {
                ForecastList(
                    forecasts = forecasts.take(3),
                    selectedForecastIndex = selectedForecastIndex,
                    onSelectedForecastIndex = onSelectedForecastIndex,
                    modifier = modifier,
                )
            }
        }
        Text(
            text = "Data provided by BMKG",
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
                .padding(vertical = 32.dp)
        )
    }
}

@Composable
private fun ErrorRegencyForecastInfoContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.bodyLarge,
        )
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
    ) {
        WeatherTemperatureInfo(
            currentTemperature = temperature,
            minTemperature = minTemperature,
            maxTemperature = maxTemperature
        )
        Spacer(
            modifier = modifier
                .height(8.dp)
        )
        Text(
            text = "$condition",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(
            modifier = modifier
                .height(8.dp)
        )
        WeatherInfoHumidityAndWind(
            humidity = humidity,
            windDirection = cardinal,
            windSpeed = "$knot kn"
        )
    }
}

@Composable
fun WeatherTemperatureInfo(
    currentTemperature: Double,
    minTemperature: Double,
    maxTemperature: Double,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextIcon(
            text = {
                Text(
                    text = minTemperature.temperature(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            leadingIcon = {
                Icon(
                    icon = WeatherQuakeIcons.ArrowDropDown,
                    contentDescription = null
                )
            }
        )
        Spacer(
            modifier = modifier
                .padding(12.dp)
        )
        Text(
            text = currentTemperature.temperature(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
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
                    contentDescription = null
                )
            },
        )
    }
}

@Composable
fun WeatherInfoHumidityAndWind(
    humidity: Int,
    windDirection: String,
    windSpeed: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
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
                    contentDescription = null
                )
            },
        )
        Spacer(
            modifier = modifier
                .width(16.dp)
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
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
fun ForecastFilterTab(
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

enum class ForecastFilter(
    val title: String,
) {
    SIXHOURS("6-Hours"),
    DAILY("Daily");

    companion object {

        fun titles(): List<String> = values().map(ForecastFilter::title)
    }
}

private val WeatherTabRowHeight = 48.dp
private val WeatherTabHeight = 40.dp

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
