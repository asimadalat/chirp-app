package com.asimorphic.chat.presentation.mapper

import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.domain.model.User

fun ChatParticipant.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl
    )
}

fun User.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = id,
        username = username,
        initials = username.take(n = 2).uppercase(),
        imageUrl = profilePictureUrl
    )
}