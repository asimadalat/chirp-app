package com.asimorphic.chat.data.chat_message

import com.asimorphic.chat.data.dto.ChatMessageDto
import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.domain.chat_message.ChatMessageService
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.core.data.network.get
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.map
import io.ktor.client.HttpClient
import kotlin.collections.buildMap

class KtorChatMessageService(
    private val httpClient: HttpClient
): ChatMessageService {

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError.Remote> {
        return httpClient.get<List<ChatMessageDto>>(
            route = "/chat/$chatId/messages",
            queryParams = buildMap {
                if (before != null)
                    this["before"] = before

                this["pageSize"] = ChatMessageConstants.PAGE_SIZE
            }
        ).map {
            it.map {
                it.toDomain()
            }
        }
    }
}