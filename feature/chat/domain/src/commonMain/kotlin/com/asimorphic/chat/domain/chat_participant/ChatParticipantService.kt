package com.asimorphic.chat.domain.chat_participant

import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.chat.domain.model.ProfilePictureUploadUrlPair
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result

interface ChatParticipantService {
    suspend fun searchParticipant(
        query: String
    ): Result<ChatParticipant, DataError.Remote>

    suspend fun getSelfParticipant(): Result<ChatParticipant, DataError.Remote>

    suspend fun getProfilePictureUploadUrl(
        mimeType: String
    ): Result<ProfilePictureUploadUrlPair, DataError.Remote>

    suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        uploadUrl: String,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote>

    suspend fun confirmProfilePictureUpload(
        publicUrl: String
    ): EmptyResult<DataError.Remote>

    suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote>
}