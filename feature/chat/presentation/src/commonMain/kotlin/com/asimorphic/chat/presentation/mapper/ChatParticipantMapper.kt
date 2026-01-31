package com.asimorphic.chat.presentation.mapper

import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi

fun ChatParticipant.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl
    )
}