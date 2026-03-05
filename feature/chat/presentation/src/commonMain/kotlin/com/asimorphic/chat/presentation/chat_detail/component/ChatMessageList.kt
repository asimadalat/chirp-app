package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.no_chats_subtitle
import chirp.feature.chat.presentation.generated.resources.no_messages
import chirp.feature.chat.presentation.generated.resources.retry
import com.asimorphic.chat.presentation.component.EmptyContentPlaceholder
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatMessageList(
    messages: List<MessageUi>,
    listState: LazyListState,
    onMessageLongClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onMessageRetryClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onPaginationRetryClick: () -> Unit,
    onDeleteMessageClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    messageWithOptionsOpen: MessageUi.SelfParticipantMessage?,
    paginationError: String?,
    isPaginationLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (messages.isEmpty()) {
        Box(
            modifier = modifier.padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyContentPlaceholder(
                title = stringResource(resource = Res.string.no_messages),
                subtitle = stringResource(resource = Res.string.no_chats_subtitle)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(
                all = 16.dp
            ),
            reverseLayout = true,
            verticalArrangement = Arrangement
                .spacedBy(
                    space = 16.dp
                ),
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
              ChatDetailListItemUi(
                  messageUi = message,
                  onMessageLongClick = onMessageLongClick,
                  onDismissMessageMenu = onDismissMessageMenu,
                  onDeleteClick = onDeleteMessageClick,
                  onRetryClick = onMessageRetryClick,
                  messageWithOptionsOpen = messageWithOptionsOpen,
                  modifier = Modifier.fillMaxWidth()
                      .animateItem()
              )
            }

            when {
                isPaginationLoading -> {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                paginationError != null -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ChirpButton(
                                text = stringResource(Res.string.retry),
                                type = ChirpButtonType.SECONDARY,
                                onClick = onPaginationRetryClick
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = paginationError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}