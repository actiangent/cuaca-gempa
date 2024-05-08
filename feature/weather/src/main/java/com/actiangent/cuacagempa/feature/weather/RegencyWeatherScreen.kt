package com.actiangent.cuacagempa.feature.weather

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.core.ui.ForecastList

@Composable
fun RegencyWeatherRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegencyWeatherViewModel = hiltViewModel(),
) {
    val regencyWeatherUiState by viewModel.regencyWeatherUiState.collectAsStateWithLifecycle()
    RegencyWeatherScreen(
        regencyWeatherUiState = regencyWeatherUiState,
        onSaveRegency = viewModel::saveRegency,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
private fun RegencyWeatherScreen(
    regencyWeatherUiState: RegencyWeatherUiState,
    onSaveRegency: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (regencyWeatherUiState) {
        is RegencyWeatherUiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is RegencyWeatherUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = onBackClick,
                            ) {
                                Icon(
                                    icon = WeatherQuakeIcons.ArrowBack,
                                    contentDescription = null,
                                )
                            }
                        },
                        actions = {
                            var isSaved by remember {
                                mutableStateOf(regencyWeatherUiState.regency.isSaved)
                            }
                            IconButton(
                                onClick = {
                                    isSaved = !isSaved
                                    onSaveRegency(isSaved)
                                },
                            ) {
                                Crossfade(
                                    targetState = isSaved,
                                    animationSpec = tween(500)
                                ) { saved ->
                                    if (saved) {
                                        Icon(
                                            icon = WeatherQuakeIcons.Done,
                                            contentDescription = null,
                                        )
                                    } else {
                                        Icon(
                                            icon = WeatherQuakeIcons.Add,
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }
                        },
                        backgroundColor = MaterialTheme.colorScheme.background,
                        elevation = 0.dp
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    val saveableRegency = regencyWeatherUiState.regency
                    val forecasts = regencyWeatherUiState.forecasts
                    ForecastSummary(
                        regency = saveableRegency.regency,
                        forecast = forecasts.first(),
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                    )
                    Text(
                        text = "24-Hour Forecast",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                    ForecastList(forecasts = forecasts)
                }
            }
        }

        is RegencyWeatherUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "An error occurred",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

