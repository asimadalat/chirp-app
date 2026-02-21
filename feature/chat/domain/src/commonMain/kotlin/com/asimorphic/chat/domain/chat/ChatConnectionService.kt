package com.asimorphic.chat.domain.chat

import com.asimorphic.chat.domain.exception.ConnectionError
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ConnectionState
import com.asimorphic.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatConnectionService {
    val chatMessages: Flow<ChatMessage>

    val connectionState: StateFlow<ConnectionState>

    suspend fun sendChatMessage(
        message: ChatMessage
    ): EmptyResult<ConnectionError>
}