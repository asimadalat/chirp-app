package com.asimorphic.chat.presentation.create_chat

import com.asimorphic.chat.domain.model.Chat

sealed interface CreateChatEvent {
    data class OnChatCreated(val chat: Chat): CreateChatEvent
}