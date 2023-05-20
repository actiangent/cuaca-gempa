package com.actiangent.cuacagempa.core.designsystem.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PermissionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    text: String,
    onConfirmText: String,
    onDismissText: String,
    isPermanentlyDeclined: Boolean,
    requestPermissionRationaleText: String,
    goToAppSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isPermanentlyDeclined) {
        PermissionInfoDialog(
            onConfirm = goToAppSetting,
            onDismiss = onDismiss,
            title = title,
            text = requestPermissionRationaleText,
            onConfirmText = "Go to app setting",
            onDismissText = onDismissText,
            modifier = modifier
        )
    } else {
        PermissionInfoDialog(
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            title = title,
            text = text,
            onConfirmText = onConfirmText,
            onDismissText = onDismissText,
            modifier = modifier
        )
    }
}

@Composable
private fun PermissionInfoDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    text: String,
    onConfirmText: String,
    onDismissText: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = text) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(text = onConfirmText) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = onDismissText) }
        },
        modifier = modifier
    )
}

@Preview(showBackground = false)
@Composable
private fun PermissionDialogPreview() {
    PermissionDialog(
        {},
        {},
        "Title",
        "Text",
        "Yes",
        "No",
        false,
        "",
        {},
    )
}