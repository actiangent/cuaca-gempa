package com.actiangent.cuacagempa.core.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

class MultiSelectionState(enabled: Boolean = false) {
    var enabled by mutableStateOf(enabled)

    companion object {
        val Saver: Saver<MultiSelectionState, Boolean> = Saver(
            save = { it.enabled },
            restore = { MultiSelectionState(enabled = it) },
        )
    }
}

@Composable
fun rememberMultiSelectionState(): MultiSelectionState {
    return rememberSaveable(saver = MultiSelectionState.Saver) {
        MultiSelectionState()
    }
}

fun <T> LazyListScope.multiSelectionItems(
    items: List<T>,
    key: ((T) -> Any)?,
    onClick: (T) -> Unit,
    onLongClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit,
) = items(
    items = items,
    key = key,
    itemContent = { item ->
        MultiSelectionContainer(
            onClick = { onClick(item) },
            onLongClick = { onLongClick(item) },
            modifier = modifier,
        ) {
            itemContent(item)
        }
    },
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MultiSelectionContainer(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
    ) {
        content()
    }
}