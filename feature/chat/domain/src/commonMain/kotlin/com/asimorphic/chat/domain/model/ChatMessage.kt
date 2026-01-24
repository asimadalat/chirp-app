package com.asimorphic.chat.domain.model

import kotlin.time.Instant

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val createdAt: Instant
)
