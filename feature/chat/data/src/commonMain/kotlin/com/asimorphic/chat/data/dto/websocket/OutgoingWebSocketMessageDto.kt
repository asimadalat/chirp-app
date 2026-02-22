package com.asimorphic.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingWebSocketMessageType {
    NEW_MESSAGE
}

@Serializable
sealed interface OutgoingWebSocketMessageDto {
    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String,
        val type: OutgoingWebSocketMessageType = OutgoingWebSocketMessageType.NEW_MESSAGE
    ): OutgoingWebSocketMessageDto
}