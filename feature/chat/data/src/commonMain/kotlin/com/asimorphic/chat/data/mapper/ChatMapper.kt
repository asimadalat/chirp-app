package com.asimorphic.chat.data.mapper

import com.asimorphic.chat.data.dto.ChatDto
import com.asimorphic.chat.domain.model.Chat
import kotlin.time.Instant

fun ChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.parse(input = lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}