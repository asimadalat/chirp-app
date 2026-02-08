@file:OptIn(ExperimentalComposeUiApi::class)

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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.chat.presentation.chat_detail.component.AdaptiveRoundedCornerColumn
import com.asimorphic.chat.presentation.chat_detail.component.ChatDetailHeader
import com.asimorphic.chat.presentation.chat_detail.component.ChatMessageList
import com.asimorphic.chat.presentation.chat_detail.component.MessageEntryBox
import com.asimorphic.chat.presentation.component.ChatHeader
import com.asimorphic.chat.presentation.model.ChatUi
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.presentation.util.UiText
import com.asimorphic.core.presentation.util.clearFocusOnTap
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun ChatDetailRoot(
    chatId: String?,
    isChatListPresent: Boolean,
    onNavigateBack: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = chatId) {
        viewModel.onAction(action = ChatDetailAction.OnSelectChat(chatId = chatId))
    }

    BackHandler(
        enabled = !isChatListPresent
    ) {
        viewModel.onAction(action = ChatDetailAction.OnSelectChat(chatId = null))
        onNavigateBack()
    }

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
            isChatListPresent  = false,
            onAction = {}
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
private fun ChatDetailMessagesPreview() {
    ChirpTheme(darkMode = true) {
        ChatDetailScreen(
            state = ChatDetailState(
                messageTextFieldState = rememberTextFieldState(
                    initialText = "This is a new message!"
                ),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = "0",
                    selfParticipant = ChatParticipantUi(
                        id = "0",
                        username = "Asim",
                        initials = "AA",
                    ),
                    otherParticipants = listOf(
                        ChatParticipantUi(
                            id = "1",
                            username = "John",
                            initials = "JD",
                        ),
                        ChatParticipantUi(
                            id = "2",
                            username = "Matt",
                            initials = "MT",
                        )
                    ),
                    lastMessage = ChatMessage(
                        id = "0",
                        chatId = "0",
                        content = "The quick brown fox jumped over the lazy dog.",
                        createdAt = Clock.System.now(),
                        senderId = "0"
                    ),
                    lastMessageSender = "Asim"
                ),
                messages = (1..20).map {
                    if (it % 2 == 0) {
                        MessageUi.SelfParticipantMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            isMenuOpen = false,
                            formattedSentTime = UiText.DynamicString(value = "Tuesday, May 4")
                        )
                    } else {
                        MessageUi.OtherParticipantMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            sender = ChatParticipantUi(
                                id = Uuid.random().toString(),
                                username = "John",
                                initials = "JD"
                            ),
                            formattedSentTime = UiText.DynamicString(value = "Friday, Aug 20"),
                        )
                    }
                }
            ),
            isChatListPresent = true,
            onAction = {}
        )
    }
}