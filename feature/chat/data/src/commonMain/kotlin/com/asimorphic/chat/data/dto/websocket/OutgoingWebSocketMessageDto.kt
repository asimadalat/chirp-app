package com.asimorphic.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingWebSocketMessageType {
    NEW_MESSAGE
}

@Serializable
sealed class OutgoingWebSocketMessageDto(
    val type: OutgoingWebSocketMessageType
) {
    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String
    ): OutgoingWebSocketMessageDto(
        type = OutgoingWebSocketMessageType.NEW_MESSAGE
    )
}