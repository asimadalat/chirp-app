package com.asimorphic.chat.presentation.manage_chat

sealed interface ManageChatEvent {
    data object OnParticipantsAdded: ManageChatEvent
}