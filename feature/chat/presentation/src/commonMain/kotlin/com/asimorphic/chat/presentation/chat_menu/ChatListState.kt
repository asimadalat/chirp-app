package com.asimorphic.chat.presentation.chat_menu

import com.asimorphic.chat.presentation.model.ChatUi
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.presentation.util.UiText

data class ChatListState(
    val chats: List<ChatUi> = emptyList(),
    val isLoadingChats: Boolean = false,
    val selectedChatId: String? = null,
    val selfParticipant: ChatParticipantUi? = null,
    val error: UiText? = null,
    val isMenuOpen: Boolean = false,
    val showLogoutConfirmation: Boolean = false
)