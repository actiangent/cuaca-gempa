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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.actiangent.cuacagempa.core.designsystem.component.PermissionDialog
import com.actiangent.cuacagempa.core.designsystem.theme.WeatherQuakeTheme
import com.actiangent.cuacagempa.ui.WeatherQuakeApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone

private const val COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when (isGranted) {
            true -> {
                onLocationGranted()
            }
            false -> {
                onLocationDenied()
            }
        }
    }

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
            DisposableEffect(uiState) {
                when (val state = uiState) {
                    is MainActivityUiState.Success -> {
                        // if (!state.data.isLocationCached) {
                            safeRequestLocation()
                        // }
                    }
                    else -> Unit
                }
                onDispose {}
            }

            WeatherQuakeTheme {
                Box {
                    val appErrorState by viewModel.errorState.collectAsStateWithLifecycle()
                    val snackbarState = remember { SnackbarHostState() }

                    appErrorState.showPermissionDialog?.let { message ->
                        PermissionDialog(
                            onConfirm = {
                                viewModel.permissionDialogShown()
                                safeRequestLocation()
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
                        Snackbar(snackbarData = snackbarData)
                    }

                    WeatherQuakeApp(requestLocation = { safeRequestLocation() })
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

    private fun safeRequestLocation() {
        when {
            (hasPermission(COARSE_LOCATION_PERMISSION)) -> {
                onLocationGranted()
            }
            (!hasPermission(COARSE_LOCATION_PERMISSION)) -> {
                locationPermissionLauncher.launch(COARSE_LOCATION_PERMISSION)
            }
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