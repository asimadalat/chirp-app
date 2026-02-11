package com.asimorphic.chat.domain.model

data class ChatMessageWithSender(
    val sender: ChatParticipant,
    val message: ChatMessage,
    val deliveryStatus: ChatMessageDeliveryStatus?
)
