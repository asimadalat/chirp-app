package com.asimorphic.chat.presentation.component.manage_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.presentation.util.UiText

data class ManageChatState(
    val queryTextFieldState: TextFieldState = TextFieldState(),
    val existingChatParticipants:List<ChatParticipantUi> = emptyList(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null,
    val isSearchingParticipants: Boolean = false,
    val isSubmitting: Boolean = false,
    val submitError: UiText? = null,
    val canAddParticipant: Boolean = false
)