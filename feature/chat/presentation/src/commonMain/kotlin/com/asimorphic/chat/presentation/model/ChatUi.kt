package com.asimorphic.chat.presentation.model

import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi

data class ChatUi(
    val id: String,
    val selfParticipant: ChatParticipantUi,
    val otherParticipants: List<ChatParticipantUi>,
    val lastMessage: ChatMessage?,
    val lastMessageSender: String?
)
