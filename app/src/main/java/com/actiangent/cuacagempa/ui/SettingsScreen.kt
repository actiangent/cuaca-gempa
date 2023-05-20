package com.actiangent.cuacagempa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    onLocationRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // val locationState by viewModel.location.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
//        LocationSetting(
//            currentLocation = locationState.provinceName,
//            onLocationRequest = onLocationRequest
//        )
    }
}

@Composable
private fun LocationSetting(
    currentLocation: String,
    onLocationRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Location", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "Your Location: $currentLocation")
        Spacer(modifier = Modifier.padding(top = 16.dp))
        TextButton(
            onClick = { onLocationRequest() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Refresh location")
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    Surface {
        SettingsScreen(onLocationRequest = { })
    }
}

