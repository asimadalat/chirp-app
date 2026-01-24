package com.asimorphic.chat.domain.model

data class MessageWithSender(
    val sender: ChatParticipant,
    val message: ChatMessage,
    val deliveryStatus: ChatMessageDeliveryStatus?
)
