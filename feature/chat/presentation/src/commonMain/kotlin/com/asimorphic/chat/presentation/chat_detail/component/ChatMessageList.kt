package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.no_chats_subtitle
import chirp.feature.chat.presentation.generated.resources.no_messages
import com.asimorphic.chat.presentation.component.EmptyListPlaceholder
import com.asimorphic.chat.presentation.model.MessageUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatMessageList(
    messages: List<MessageUi>,
    listState: LazyListState,
    onMessageLongClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onMessageRetryClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onDeletMessageClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (messages.isEmpty()) {
        Box(
            modifier = modifier.padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyListPlaceholder(
                title = stringResource(resource = Res.string.no_messages),
                subtitle = stringResource(resource = Res.string.no_chats_subtitle)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(all = 16.dp),
            reverseLayout = true
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                // TODO: Finish list items
            }
        }
    }

}