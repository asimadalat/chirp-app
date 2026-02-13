package com.asimorphic.chat.data.chat

import com.asimorphic.chat.data.dto.ChatDto
import com.asimorphic.chat.data.dto.request.CreateChatRequest
import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.domain.chat.ChatService
import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.core.data.network.delete
import com.asimorphic.core.data.network.get
import com.asimorphic.core.data.network.post
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.map
import com.asimorphic.core.domain.util.onEmpty
import io.ktor.client.HttpClient
import kotlin.collections.List
import kotlin.collections.map

class KtorChatService(
    private val httpClient: HttpClient
): ChatService {
    override suspend fun createChat(
        otherUserIds: List<String>
    ): Result<Chat, DataError.Remote> {
        return httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map {
            it.toDomain()
        }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatDto>>(
            route = "/chat"
        ).map { chatDtos ->
            chatDtos.map {
                it.toDomain()
            }
        }
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return httpClient.get<ChatDto>(
                route = "/chat/$chatId"
        ).map {
            it.toDomain()
        }
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/chat/$chatId/leave"
        ).onEmpty()
    }
}