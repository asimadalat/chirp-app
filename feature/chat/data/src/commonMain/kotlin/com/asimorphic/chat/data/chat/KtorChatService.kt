package com.asimorphic.chat.data.chat

import com.asimorphic.chat.data.dto.ChatDto
import com.asimorphic.chat.data.dto.request.CreateChatRequest
import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.domain.chat.ChatService
import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.core.data.network.post
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.map
import io.ktor.client.HttpClient

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
}