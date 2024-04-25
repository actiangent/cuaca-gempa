package com.actiangent.cuacagempa.feature.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actiangent.cuacagempa.core.designsystem.component.multiSelectionItems
import com.actiangent.cuacagempa.core.designsystem.component.rememberMultiSelectionState
import com.actiangent.cuacagempa.core.designsystem.icon.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun SearchRegencyScreen(
    navigateUp: () -> Unit,
    navigateToRegencyWeather: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchRegencyViewModel = hiltViewModel(),
) {
    val userRegencyUiState = viewModel.userRegencyUiState.collectAsStateWithLifecycle().value
    val searchRegencyUiState = viewModel.searchRegencyUiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val query by viewModel.searchQuery.collectAsStateWithLifecycle()
                var active by rememberSaveable { mutableStateOf(false) }

                AnimatedVisibility(
                    visible = !active,
                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Manage city/regency",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = navigateUp,
                            ) {
                                Icon(
                                    icon = WeatherQuakeIcons.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }

                SearchBar(
                    query = query,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    onSearch = {},
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = { Text(text = "Search city/regency") },
                    leadingIcon = {
                        Icon(
                            icon = WeatherQuakeIcons.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (active) {
                            IconButton(
                                onClick = {
                                    viewModel.setSearchQuery("")
                                    active = false
                                },
                            ) {
                                Icon(
                                    icon = WeatherQuakeIcons.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                ) {
                    when (searchRegencyUiState) {
                        SearchRegencyUiState.Loading -> Unit
                        SearchRegencyUiState.EmptyQuery -> Unit
                        is SearchRegencyUiState.Success -> {
                            val saveableRegencies = searchRegencyUiState.regencies
                            LazyColumn {
                                items(
                                    items = saveableRegencies,
                                    key = { saveableRegency -> saveableRegency.regency.id }
                                ) { saveableRegency ->
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                text = saveableRegency.regency.name,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        },
                                        supportingContent = {
                                            Text(
                                                text = saveableRegency.regency.province.name,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        },
                                        leadingContent = {
                                            Icon(
                                                icon = WeatherQuakeIcons.PinDrop,
                                                contentDescription = null,
                                            )
                                        },
                                        trailingContent = {
                                            var isSaved by remember {
                                                mutableStateOf(
                                                    saveableRegency.isSaved
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    isSaved = !isSaved
                                                    viewModel.saveRegency(
                                                        saveableRegency.regency.id,
                                                        isSaved
                                                    )
                                                },
                                            ) {
                                                Crossfade(
                                                    targetState = isSaved,
                                                    animationSpec = tween(500)
                                                ) { saved ->
                                                    if (saved) {
                                                        Icon.Icon(
                                                            icon = WeatherQuakeIcons.Check,
                                                            contentDescription = null,
                                                        )
                                                    } else {
                                                        Icon.Icon(
                                                            icon = WeatherQuakeIcons.Add,
                                                            contentDescription = null,
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        modifier = modifier
                                            .clickable {
                                                navigateToRegencyWeather(saveableRegency.regency.id)
                                            }
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        is SearchRegencyUiState.Error -> Unit
                    }
                }

            }
        },
    ) { paddingValues ->
        Column {
            Spacer(
                modifier = Modifier
                    .height(TOP_APP_BAR_HEIGHT + SEARCH_BAR_HEIGHT + 16.dp)
            )
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                val multiSelectionState = rememberMultiSelectionState()

                val selectedRegencyIds = remember { mutableStateListOf<String>() }

                LaunchedEffect(
                    key1 = multiSelectionState,
                    key2 = selectedRegencyIds.size,
                ) {
                    if (multiSelectionState.enabled && selectedRegencyIds.isEmpty()) {
                        multiSelectionState.enabled = false
                    }
                }

                when (userRegencyUiState) {
                    UserRegencyUiState.Loading -> Unit
                    is UserRegencyUiState.Success -> {
                        val userRegencies = userRegencyUiState.regencies
                        LazyColumn {
                            multiSelectionItems(
                                items = userRegencies,
                                key = { regency -> regency.id },
                                onClick = { regency ->
                                    if (multiSelectionState.enabled) {
                                        if (regency.id in selectedRegencyIds) {
                                            selectedRegencyIds.remove(regency.id)
                                        } else {
                                            selectedRegencyIds.add(regency.id)
                                        }
                                    }
                                },
                                onLongClick = { regency ->
                                    multiSelectionState.enabled = true
                                    selectedRegencyIds.add(regency.id)
                                }
                            ) { regency ->
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = regency.name,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = regency.province.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    leadingContent = {
                                        Icon(
                                            icon = WeatherQuakeIcons.PinDrop,
                                            contentDescription = null,
                                        )
                                    },
                                    trailingContent = {
                                        AnimatedVisibility(
                                            visible = regency.id in selectedRegencyIds,
                                            enter = scaleIn() + fadeIn(),
                                            exit = scaleOut() + fadeOut(),
                                        ) {
                                            Icon(
                                                icon = WeatherQuakeIcons.Check,
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                    is UserRegencyUiState.Error -> Unit
                }
            }
        }
    }
}

private val TOP_APP_BAR_HEIGHT = 64.dp
private val SEARCH_BAR_HEIGHT = 56.dp
