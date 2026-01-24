package com.asimorphic.chat.domain.model

import kotlin.time.Instant

data class Chat(
    val id: String,
    val participants: List<ChatParticipant>,
    val lastMessage: String?,
    val lastActivityAt: Instant
)
