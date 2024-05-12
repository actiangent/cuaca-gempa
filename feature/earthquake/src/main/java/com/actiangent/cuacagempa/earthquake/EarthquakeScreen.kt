package com.actiangent.cuacagempa.earthquake

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actiangent.cuacagempa.core.common.datetime.text
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons
import com.actiangent.cuacagempa.core.model.DetailedEarthquake

@Composable
fun EarthquakeRoute(
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EarthquakeViewModel = hiltViewModel(),
) {
    val earthquakeUiState by viewModel.earthquakeUiState.collectAsStateWithLifecycle()
    EarthquakeScreen(
        earthquakeUiState = earthquakeUiState,
        onSettingClick = onSettingClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EarthquakeScreen(
    earthquakeUiState: EarthquakeUiState,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Earthquake",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(
                        onClick = onSettingClick,
                    ) {
                        Icon(
                            icon = WeatherQuakeIcons.SettingsFilled,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
            )
            when (earthquakeUiState) {
                EarthquakeUiState.Loading -> {
                    Box(
                        modifier = modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is EarthquakeUiState.Success -> {
                    val recentEarthquake = earthquakeUiState.recentEarthquake
                    val latestCautiousEarthquakes = earthquakeUiState.latestCautiousEarthquakes

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Recent Earthquake",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                        }
                        item {
                            EarthquakeItem(earthquake = recentEarthquake)
                        }
                        item {
                            Text(
                                text = "Latest Cautious Earthquake",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                        }
                        itemsIndexed(
                            items = latestCautiousEarthquakes,
                            key = { index, _ -> index }
                        ) { _, earthquake ->
                            EarthquakeItem(earthquake = earthquake)
                        }
                    }
                }

                is EarthquakeUiState.Error -> {
                    Box(
                        modifier = modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = earthquakeUiState.message,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EarthquakeItem(
    earthquake: DetailedEarthquake,
    modifier: Modifier = Modifier,
) {
    Card(
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        modifier = modifier,
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = "${earthquake.regency.name}, ${earthquake.regency.province.name}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            overlineContent = {
                Text(
                    text = earthquake.earthquake.dateTime.text(),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            supportingContent = {
                Text(
                    text = "Magnitude ${earthquake.earthquake.magnitude}, Depth ${earthquake.earthquake.depth}",
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            leadingContent = {
                Icon(
                    icon = WeatherQuakeIcons.Earthquake,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color = earthquake.earthquake.magnitude.magnitudeScaleColor())
                )
            },
        )
    }
}

fun Double.magnitudeScaleColor(): Color = when {
    this in (1.0..1.9) -> Color(0xFF79AB4A)
    this in (2.0..3.9) -> Color(0xFFFBB03B)
    this in (4.0..4.9) -> Color(0xFFF15A25)
    this in (5.0..5.9) -> Color(0xFFF0452B)
    this in (6.0..6.9) -> Color(0xFFEA1C29)
    this in (7.0..7.9) -> Color(0xFFA11252)
    this >= 8.0 -> Color(0xFF57043A)
    else -> Color.Gray
}