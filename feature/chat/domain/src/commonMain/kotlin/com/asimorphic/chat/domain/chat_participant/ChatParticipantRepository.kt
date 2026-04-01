package com.asimorphic.chat.domain.chat_participant

import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result

interface ChatParticipantRepository {
    suspend fun fetchSelfParticipant(): Result<ChatParticipant, DataError>

    suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote>

    suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote>
}