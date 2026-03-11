package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import com.asimorphic.chat.presentation.model.MessageUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MessageChipListener(
    messages: List<MessageUi>,
    lazyListState: LazyListState,
    isChipVisible: Boolean,
    onShowChip: (topVisibleItemIndex: Int) -> Unit,
    onHide: () -> Unit
) {
    val isChipVisibleUpdated by rememberUpdatedState(isChipVisible)

    LaunchedEffect(lazyListState, messages) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            val total = info.totalItemsCount
            val visibleItems = info.visibleItemsInfo

            val oldestVisibleMessageIndex = visibleItems.maxOfOrNull {
                it.index
            } ?: -1
            val isAtOldestMessage = oldestVisibleMessageIndex >= total - 1
            val isAtNewestMessage = visibleItems.any { it.index == 0 }

            MessageChipScrollState(
                oldestVisibleMessageIndex = oldestVisibleMessageIndex,
                isScrollInProgress = lazyListState.isScrollInProgress,
                isAtEdgeOfList = isAtOldestMessage || isAtNewestMessage
            )
        }.distinctUntilChanged()
            .collect { (oldestVisibleIndex, isScrollInProgress, isAtEdgeOfList) ->
                val shouldShowChip = isScrollInProgress && !isAtEdgeOfList && oldestVisibleIndex >= 0

                when {
                    shouldShowChip -> onShowChip(oldestVisibleIndex)
                    !shouldShowChip && isChipVisibleUpdated -> {
                        delay(timeMillis = 1000L)
                        onHide()
                    }
                }
            }
    }
}

data class MessageChipScrollState(
    val oldestVisibleMessageIndex: Int,
    val isScrollInProgress: Boolean,
    val isAtEdgeOfList: Boolean
)