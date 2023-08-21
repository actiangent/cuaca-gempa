package com.actiangent.cuacagempa

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actiangent.cuacagempa.core.designsystem.component.PermissionDialog
import com.actiangent.cuacagempa.core.designsystem.theme.WeatherQuakeTheme
import com.actiangent.cuacagempa.ui.WeatherQuakeApp
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone

private const val COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState = it }
            }
        }

        Log.d("Current TimeZone", "onCreate: ${TimeZone.currentSystemDefault()}")

        setContent {
            val locationPermissionsState = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            ) { permissions ->
                if (permissions.values.any { granted -> granted }) {
                    onLocationGranted()
                } else {
                    onLocationDenied()
                }
            }

            WeatherQuakeLocationPermissionHandling(
                appState = uiState,
                permissionsState = locationPermissionsState,
                onLocationGranted = { onLocationGranted() },
                onLocationRevoked = { viewModel.showLocationPermissionsRevokedError() }
            )

            WeatherQuakeTheme {
                Box {
                    val appErrorState by viewModel.errorState.collectAsStateWithLifecycle()
                    val snackbarState = remember { SnackbarHostState() }

                    appErrorState.showPermissionDialog?.let { message ->
                        PermissionDialog(
                            onConfirm = {
                                viewModel.permissionDialogShown()
                                locationPermissionsState.launchMultiplePermissionRequest()
                            },
                            onDismiss = { viewModel.permissionDialogShown() },
                            title = "Please grant location permission",
                            text = "This app need to access your location",
                            onConfirmText = "Grant",
                            onDismissText = "Dismiss",
                            isPermanentlyDeclined = isLocationPermissionPermanentlyDeclined(),
                            requestPermissionRationaleText = message,
                            goToAppSetting = {
                                viewModel.permissionDialogShown()
                                openAppSetting()
                            }
                        )
                    }

                    appErrorState.locationRequestError?.let { message ->
                        lifecycleScope.launch {
                            snackbarState.showSnackbar(message = message)
                            viewModel.locationRequestErrorShown()
                        }
                    }

                    SnackbarHost(
                        modifier = Modifier.align(Alignment.BottomStart),
                        hostState = snackbarState
                    ) { snackbarData ->
                        Log.d("SnackbarHost", snackbarData.message)
                        Snackbar(snackbarData = snackbarData)
                    }

                    WeatherQuakeApp()
                }
            }
        }
    }

    private fun onLocationGranted() {
        viewModel.requestUpdatedLocation()
    }

    private fun onLocationDenied() {
        viewModel.showPermissionDialog()
    }

    private fun isLocationPermissionPermanentlyDeclined() =
        !shouldShowRequestPermissionRationale(COARSE_LOCATION_PERMISSION)

}

@Composable
private fun WeatherQuakeLocationPermissionHandling(
    appState: MainActivityUiState,
    permissionsState: MultiplePermissionsState,
    onLocationGranted: () -> Unit,
    onLocationRevoked: () -> Unit
) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner, appState) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    when (appState) {
                        is MainActivityUiState.Success -> {
                            val allPermissionsRevoked = permissionsState.permissions.size ==
                                    permissionsState.revokedPermissions.size

                            if (appState.isLocationCached) {
                                if (!allPermissionsRevoked) {
                                    onLocationGranted()
                                } else {
                                    onLocationRevoked()
                                }
                            } else {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        else -> Unit
                    }
                }
                else -> Unit
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

fun Activity.hasPermission(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED

private fun Activity.openAppSetting() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also { startActivity(it) }
}