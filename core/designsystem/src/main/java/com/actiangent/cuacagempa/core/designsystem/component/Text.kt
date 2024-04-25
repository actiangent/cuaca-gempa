package com.actiangent.cuacagempa.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextIcon(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    spacingValues: PaddingValues = PaddingValues(2.dp)
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(
                modifier = modifier
                    .padding(spacingValues)
            )
        }
        text()
        if (trailingIcon != null) {
            Spacer(
                modifier = modifier
                    .padding(spacingValues)
            )
            trailingIcon()
        }
    }
}