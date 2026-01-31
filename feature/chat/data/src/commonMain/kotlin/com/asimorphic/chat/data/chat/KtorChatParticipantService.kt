package com.asimorphic.chat.data.chat

import com.asimorphic.chat.data.dto.ChatParticipantDto
import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.domain.chat.ChatParticipantService
import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.data.network.get
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatParticipantService(
    private val httpClient: HttpClient
): ChatParticipantService {
    override suspend fun searchParticipant(
        query: String)
    : Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/chat/participants",
            queryParams = mapOf("query" to query)
        ).map {
            it.toDomain()
        }
    }
}