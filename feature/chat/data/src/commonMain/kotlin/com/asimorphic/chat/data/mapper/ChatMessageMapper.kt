package com.asimorphic.chat.data.mapper

import com.asimorphic.chat.data.dto.ChatMessageDto
import com.asimorphic.chat.domain.model.ChatMessage
import kotlin.time.Instant

fun ChatMessageDto.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = Instant.parse(input = createdAt),
        senderId = senderId
    )
}