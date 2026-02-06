package com.asimorphic.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.chat.domain.model.ConnectionState
import com.asimorphic.chat.presentation.model.ChatUi
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.core.presentation.util.UiText

data class ChatDetailState(
    val chatUi: ChatUi? = null,
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationEndReached: Boolean = false,
    val error: UiText? = null,
    val paginationError: UiText? = null,
    val chipState: ChipState = ChipState(),
    val messages: List<MessageUi> = emptyList(),
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isChatOptionsOpen: Boolean = false,
    val isNearBottom: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)

data class ChipState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false
)