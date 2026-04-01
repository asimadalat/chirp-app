package com.asimorphic.chat.data.chat_participant

import com.asimorphic.chat.data.dto.ChatParticipantDto
import com.asimorphic.chat.data.dto.request.ConfirmProfilePictureRequest
import com.asimorphic.chat.data.dto.response.ProfilePictureUploadResponse
import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.domain.chat_participant.ChatParticipantService
import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.chat.domain.model.ProfilePictureUploadUrlPair
import com.asimorphic.core.data.network.delete
import com.asimorphic.core.data.network.get
import com.asimorphic.core.data.network.post
import com.asimorphic.core.data.network.put
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.header

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

    override suspend fun getSelfParticipant(): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/chat/participants",
        ).map {
            it.toDomain()
        }
    }

    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrlPair, DataError.Remote> {
        return httpClient.post<Unit, ProfilePictureUploadResponse>(
            route = "/chat/participants/upload-profile-picture",
            queryParams = mapOf(
                "mimeType" to mimeType
            ),
            body = Unit
        ).map {
            it.toDomain()
        }
    }

    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        uploadUrl: String,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return httpClient.put(
            route = uploadUrl,
            useAbsoluteUrl = true,
            body = imageBytes
        ) {
            headers.forEach { (headerName, headerValue) ->
                header(headerName, headerValue)
            }
        }
    }

    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return httpClient.post<ConfirmProfilePictureRequest, Unit>(
            route = "/chat/participants/confirm-profile-picture",
            body = ConfirmProfilePictureRequest(publicUrl)
        )
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/chat/participants/profile-picture"
        )
    }
}