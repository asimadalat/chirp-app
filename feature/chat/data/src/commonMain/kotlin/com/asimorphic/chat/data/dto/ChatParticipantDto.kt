package com.asimorphic.chat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatParticipantDto(
    val userId: String,
    val username: String,
    val email: String,
    val profilePicUrl: String?
)