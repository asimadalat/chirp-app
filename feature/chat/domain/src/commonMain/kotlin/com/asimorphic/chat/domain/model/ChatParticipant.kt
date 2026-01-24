package com.asimorphic.chat.domain.model

data class ChatParticipant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String
) {
    val initials: String
        get() = username.take(n = 2).uppercase()
}
