package com.asimorphic.chat.data.chat_participant

import com.asimorphic.chat.domain.chat_participant.ChatParticipantRepository
import com.asimorphic.chat.domain.chat_participant.ChatParticipantService
import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
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

    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote> {
        val result = chatParticipantService.getProfilePictureUploadUrl(mimeType)
        if (result is Result.Failure)
            return result

        val urlPair = (result as Result.Success).data
        val uploadResult = chatParticipantService.uploadProfilePicture(
            imageBytes = imageBytes,
            uploadUrl = urlPair.uploadUrl,
            headers = urlPair.headers
        )

        if (uploadResult is Result.Failure)
            return uploadResult

        return chatParticipantService
            .confirmProfilePictureUpload(
                publicUrl = urlPair.publicUrl
            )
            .onSuccess {
                val currentAuthCredential = sessionService.observeAuthCredential().first()
                sessionService.set(
                    currentAuthCredential?.copy(
                        user = currentAuthCredential.user.copy(
                            profilePictureUrl = urlPair.publicUrl
                        )
                    )
                )
            }
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return chatParticipantService
            .deleteProfilePicture()
            .onSuccess {
                val authCredential = sessionService.observeAuthCredential().first()
                sessionService.set(
                    authCredential?.copy(
                        user = authCredential.user.copy(
                            profilePictureUrl = null
                        )
                    )
                )
            }
    }

}