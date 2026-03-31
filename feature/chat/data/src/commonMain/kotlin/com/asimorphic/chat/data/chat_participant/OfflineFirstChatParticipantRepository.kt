package com.asimorphic.chat.data.chat_participant

import com.asimorphic.chat.domain.chat_participant.ChatParticipantRepository
import com.asimorphic.chat.domain.chat_participant.ChatParticipantService
import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.onSuccess
import kotlinx.coroutines.flow.first

class OfflineFirstChatParticipantRepository(
    private val sessionService: SessionService,
    private val chatParticipantService: ChatParticipantService
): ChatParticipantRepository {
    override suspend fun fetchSelfParticipant(): Result<ChatParticipant, DataError> {
        return chatParticipantService
            .getSelfParticipant()
            .onSuccess { participant ->
                val currentAuthCredential = sessionService.observeAuthCredential().first()
                sessionService.set(
                    currentAuthCredential?.copy(
                        user = currentAuthCredential.user.copy(
                            id = participant.userId,
                            username = participant.username,
                            profilePictureUrl = participant.profilePictureUrl
                        )
                    )
                )
            }
    }

}