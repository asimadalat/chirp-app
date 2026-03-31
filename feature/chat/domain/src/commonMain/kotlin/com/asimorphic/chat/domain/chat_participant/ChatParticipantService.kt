package com.asimorphic.chat.domain.chat_participant

import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result

interface ChatParticipantService {
    suspend fun searchParticipant(
        query: String
    ): Result<ChatParticipant, DataError.Remote>

    suspend fun getSelfParticipant(): Result<ChatParticipant, DataError.Remote>
}