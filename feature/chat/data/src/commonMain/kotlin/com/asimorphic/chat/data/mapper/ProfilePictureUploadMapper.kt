package com.asimorphic.chat.data.mapper

import com.asimorphic.chat.data.dto.response.ProfilePictureUploadResponse
import com.asimorphic.chat.domain.model.ProfilePictureUploadUrlPair

fun ProfilePictureUploadResponse.toDomain(): ProfilePictureUploadUrlPair {
    return ProfilePictureUploadUrlPair(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers
    )
}