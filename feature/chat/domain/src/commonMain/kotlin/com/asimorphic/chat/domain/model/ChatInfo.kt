package com.asimorphic.chat.domain.model

data class ChatInfo(
    val chat: Chat,
    val messages: List<MessageWithSender>
)
