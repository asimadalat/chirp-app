package com.asimorphic.chat.data.mapper

import com.asimorphic.chat.data.dto.ChatParticipantDto
import com.asimorphic.chat.domain.model.ChatParticipant

fun ChatParticipantDto.toDomain(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePicUrl
    )
}