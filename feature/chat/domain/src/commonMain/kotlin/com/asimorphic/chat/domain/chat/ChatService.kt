package com.asimorphic.chat.domain.chat

import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result

interface ChatService {
    suspend fun createChat(
        otherUserIds: List<String>
    ): Result<Chat, DataError.Remote>
}