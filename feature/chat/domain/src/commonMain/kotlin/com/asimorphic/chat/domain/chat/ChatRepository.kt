package com.asimorphic.chat.domain.chat

import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.chat.domain.model.ChatInfo
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun createChat(
        otherUserIds: List<String>
    ): Result<Chat, DataError.Remote>

    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>

    suspend fun fetchChatById(
        chatId: String
    ): EmptyResult<DataError.Remote>

    suspend fun leaveChat(
        chatId: String
    ): EmptyResult<DataError.Remote>

    fun getChats(): Flow<List<Chat>>

    fun getChatInfoById(
        chatId: String
    ): Flow<ChatInfo>
}