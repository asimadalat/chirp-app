package com.asimorphic.chat.presentation.create_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.presentation.util.UiText

data class CreateChatState(
    val queryTextFieldState: TextFieldState = TextFieldState(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null,
    val isSearchingParticipants: Boolean = false,
    val isCreatingChat: Boolean = false,
    val canAddParticipant: Boolean = false
)