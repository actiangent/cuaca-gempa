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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actiangent.cuacagempa.core.designsystem.component.multiSelectionItems
import com.actiangent.cuacagempa.core.designsystem.component.rememberMultiSelectionState
import com.actiangent.cuacagempa.core.designsystem.icon.Icon.Companion.Icon
import com.actiangent.cuacagempa.core.designsystem.icon.WeatherQuakeIcons

@Composable
fun UserRegencyRoute(
    onBackClick: () -> Unit,
    onRegencyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserRegencyViewModel = hiltViewModel(),
) {
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()

    val userRegenciesUiState by viewModel.userRegenciesUiState.collectAsStateWithLifecycle()
    val searchRegencyUiState by viewModel.searchRegencyUiState.collectAsStateWithLifecycle()

    UserRegencyScreen(
        query = query,
        onQueryChange = viewModel::setSearchQuery,
        userRegenciesUiState = userRegenciesUiState,
        searchRegencyUiState = searchRegencyUiState,
        onBackClick = onBackClick,
        onRegencyClick = onRegencyClick,
        onSaveRegency = viewModel::saveRegency,
        onUnsaveRegencies = viewModel::unsaveRegencies,
        modifier = modifier,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
private fun UserRegencyScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    userRegenciesUiState: UserRegenciesUiState,
    searchRegencyUiState: SearchRegencyUiState,
    onSaveRegency: (String, Boolean) -> Unit,
    onUnsaveRegencies: (Set<String>) -> Unit,
    onBackClick: () -> Unit,
    onRegencyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val multiSelectionState = rememberMultiSelectionState()
    val selectedRegencyIds = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                var showTopAppBar by rememberSaveable { mutableStateOf(false) }

                AnimatedVisibility(
                    visible = !showTopAppBar,
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
                                onClick = onBackClick,
                            ) {
                                Icon(
                                    icon = WeatherQuakeIcons.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            if (multiSelectionState.enabled) {
                                IconButton(
                                    onClick = {
                                        val regencyIds = selectedRegencyIds.toSet()
                                        onUnsaveRegencies(regencyIds)

                                        multiSelectionState.enabled = false
                                    },
                                ) {
                                    Icon(
                                        icon = WeatherQuakeIcons.RemoveDone,
                                        contentDescription = null
                                    )
                                }
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
                    onQueryChange = onQueryChange,
                    onSearch = {},
                    active = showTopAppBar,
                    onActiveChange = {
                        showTopAppBar = it
                    },
                    placeholder = { Text(text = "Search city/regency") },
                    leadingIcon = {
                        Icon(
                            icon = WeatherQuakeIcons.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (showTopAppBar) {
                            IconButton(
                                onClick = {
                                    onQueryChange("")
                                    showTopAppBar = false
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
                        SearchRegencyUiState.Loading -> {
                            Box(
                                modifier = modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

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
                                                    onSaveRegency(
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
                                        modifier = modifier
                                            .clickable {
                                                onRegencyClick(saveableRegency.regency.id)
                                            }
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        is SearchRegencyUiState.Error -> {
                            Box(
                                modifier = modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = searchRegencyUiState.message,
                                    fontWeight = FontWeight.Light,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                }

            }
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(paddingValues)
                .padding(top = 16.dp),
        ) {
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LaunchedEffect(
                    key1 = multiSelectionState,
                    key2 = selectedRegencyIds.size,
                ) {
                    if (multiSelectionState.enabled && selectedRegencyIds.isEmpty()) {
                        multiSelectionState.enabled = false
                    }
                }

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
                        val userRegencies = userRegenciesUiState.regencies
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
                                                icon = WeatherQuakeIcons.Done,
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
