package com.asimorphic.chat.presentation.chat_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asimorphic.chat.presentation.chat_detail.component.AdaptiveRoundedCornerColumn
import com.asimorphic.chat.presentation.chat_detail.component.ChatDetailHeader
import com.asimorphic.chat.presentation.chat_detail.component.ChatMessageList
import com.asimorphic.chat.presentation.chat_detail.component.MessageEntryBox
import com.asimorphic.chat.presentation.component.ChatHeader
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.presentation.util.clearFocusOnTap
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatDetailRoot(
    isChatListPresent: Boolean,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChatDetailScreen(
        state = state,
        isChatListPresent = isChatListPresent,
        onAction = viewModel::onAction
    )
}

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    isChatListPresent: Boolean,
    onAction: (ChatDetailAction) -> Unit,
) {
    val screenSizeType = currentDeviceScreenSizeType()
    val chatMessageListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = if (!screenSizeType.isWideView)
                            MaterialTheme.colorScheme.surface
                         else
                            MaterialTheme.colorScheme.extended.surfaceOutline,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .padding(paddingValues = paddingValues)
                .then(
                    other = if (screenSizeType.isWideView)
                                Modifier.padding(horizontal = 8.dp)
                            else
                                Modifier
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AdaptiveRoundedCornerColumn(
                    isCornersRounded = screenSizeType.isWideView,
                    modifier = Modifier
                        .weight(
                            weight = 1f
                        )
                        .fillMaxWidth()
                ) {
                    ChatHeader {
                        ChatDetailHeader(
                            chatUi = state.chatUi,
                            isChatListPresent = isChatListPresent,
                            isMenuDropdownOpen = state.isChatOptionsOpen,
                            onChatOptionsClick = {
                                onAction(ChatDetailAction.OnChatOptionsClick)
                            },
                            onDismissChatOptions = {
                                onAction(ChatDetailAction.OnDismissChatOptions)
                            },
                            onManageChatClick = {
                                onAction(ChatDetailAction.OnPeopleClick)
                            },
                            onLeaveChatClick = {
                                onAction(ChatDetailAction.OnLeaveChatClick)
                            },
                            onBackClick = {
                                onAction(ChatDetailAction.OnBackClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    ChatMessageList(
                        messages = state.messages,
                        listState = chatMessageListState,
                        onMessageLongClick = { message ->
                            onAction(ChatDetailAction.OnMessageLongClick(message))
                        },
                        onMessageRetryClick = { message ->
                            onAction(ChatDetailAction.OnRetryClick(message))
                        },
                        onDeleteMessageClick = { message ->
                            onAction(ChatDetailAction.OnDeleteMessageClick(message))
                        },
                        onDismissMessageMenu = {
                            onAction(ChatDetailAction.OnDismissMessageOptions)
                        },
                        modifier = Modifier.fillMaxWidth()
                            .weight(weight = 1f),
                    )

                    AnimatedVisibility(
                        visible = !screenSizeType.isWideView && state.chatUi != null
                    ) {
                        MessageEntryBox(
                            messageTextFieldState = state.messageTextFieldState,
                            connectionState = state.connectionState,
                            isTextInputEnabled = state.canSendMessage,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (screenSizeType.isWideView) {
                    Spacer(modifier = Modifier.height(height = 8.dp))
                }

                AnimatedVisibility(
                    visible = screenSizeType.isWideView && state.chatUi != null
                ) {
                    MessageEntryBox(
                        messageTextFieldState = state.messageTextFieldState,
                        connectionState = state.connectionState,
                        isTextInputEnabled = state.canSendMessage,
                        onSendClick = {
                            onAction(ChatDetailAction.OnSendMessageClick)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatDetailEmptyPreview() {
    ChirpTheme {
        ChatDetailScreen(
            state = ChatDetailState(),
            isChatListPresent = true,
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun ChatDetailMessagesPreview() {
    ChirpTheme {
        ChatDetailScreen(
            state = ChatDetailState(),
            isChatListPresent = true,
            onAction = {}
        )
    }
}