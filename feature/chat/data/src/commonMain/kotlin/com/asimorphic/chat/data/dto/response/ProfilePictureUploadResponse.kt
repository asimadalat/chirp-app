package com.asimorphic.chat.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfilePictureUploadResponse(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
