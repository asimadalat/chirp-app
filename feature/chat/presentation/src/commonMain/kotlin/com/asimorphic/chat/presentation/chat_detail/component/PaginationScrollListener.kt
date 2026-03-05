package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PaginationScrollListener(
    itemCount: Int,
    lazyListState: LazyListState,
    isEndReached: Boolean,
    onNearTop: () -> Unit,
    isPaginationLoading: Boolean
) {
    val updatedItemCount by rememberUpdatedState(itemCount)
    val isEndReached by rememberUpdatedState(isEndReached)
    val isPaginationLoading by rememberUpdatedState(isPaginationLoading)

    var lastTriggerItemCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            val total = info.totalItemsCount
            val topVisibleIndex = info.visibleItemsInfo.lastOrNull()?.index
            val remainingItems = if (topVisibleIndex != null) {
                                     total - topVisibleIndex - 1
                                 } else {
                                     null
                                 }

            PaginationScrollState(
                currentItemCount = updatedItemCount,
                isEligible = remainingItems != null &&
                    remainingItems <= 5 &&
                    !isEndReached &&
                    !isPaginationLoading
            )
        }.distinctUntilChanged()
            .collect { (itemCount, isEligible) ->
                val shouldTrigger = isEligible && itemCount > lastTriggerItemCount

                if (shouldTrigger) {
                    lastTriggerItemCount = itemCount
                    onNearTop()
                }
            }
    }
}

data class PaginationScrollState(
    val currentItemCount: Int,
    val isEligible: Boolean
)