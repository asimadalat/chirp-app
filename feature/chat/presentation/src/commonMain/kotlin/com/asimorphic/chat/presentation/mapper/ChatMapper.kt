package com.asimorphic.chat.presentation.mapper

import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.chat.presentation.model.ChatUi

fun Chat.toUi(selfParticipantId: String): ChatUi {
    val (self, other) = participants.partition {
        it.userId == selfParticipantId
    }

    return ChatUi(
        id = id,
        selfParticipant = self.first().toUi(),
        otherParticipants = other.map { it.toUi() },
        lastMessage = lastMessage,
        lastMessageSender = this.participants.find {
            it.userId == lastMessage?.senderId
        }?.username
    )
}