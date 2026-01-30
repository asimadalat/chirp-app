package com.asimorphic.chat.presentation.create_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.core.designsystem.component.profile_picture.ProfilePictureUi
import com.asimorphic.core.presentation.util.UiText

data class CreateChatState(
    val queryTextFieldState: TextFieldState = TextFieldState(),
    val selectedChatParticipants: List<ProfilePictureUi> = emptyList(),
    val currentSearchResult: ProfilePictureUi? = null,
    val searchError: UiText? = null,
    val isAddingParticipants: Boolean = false,
    val isLoadingParticipants: Boolean = false,
    val isCreatingChat: Boolean = false,
    val canAddParticipant: Boolean = false
)