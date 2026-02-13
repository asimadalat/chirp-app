package com.asimorphic.chat.domain.chat

import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result

interface ChatService {
    suspend fun createChat(
        otherUserIds: List<String>
    ): Result<Chat, DataError.Remote>

    suspend fun getChats():
            Result<List<Chat>, DataError.Remote>

    suspend fun getChatById(
        chatId: String
    ): Result<Chat, DataError.Remote>

    suspend fun leaveChat(
        chatId: String
    ): EmptyResult<DataError.Remote>
}