package com.asimorphic.chat.domain.chat_participant

import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result

interface ChatParticipantRepository {
    suspend fun fetchSelfParticipant(): Result<ChatParticipant, DataError>
}