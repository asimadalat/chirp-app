package com.asimorphic.chat.domain.chat

import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatConnectionService {
    val chatMessages: Flow<ChatMessage>

    val connectionState: StateFlow<ConnectionState>
}