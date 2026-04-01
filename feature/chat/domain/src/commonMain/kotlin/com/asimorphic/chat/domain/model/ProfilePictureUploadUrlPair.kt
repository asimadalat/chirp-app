package com.asimorphic.chat.domain.model

data class ProfilePictureUploadUrlPair(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
