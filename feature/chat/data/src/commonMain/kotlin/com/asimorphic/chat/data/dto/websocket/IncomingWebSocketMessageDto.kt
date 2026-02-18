package com.asimorphic.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingWebSocketMessageType {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    PROFILE_PIC_UPDATED,
    CHAT_PARTICIPANTS_CHANGED,
    ERROR
}

@Serializable
sealed class IncomingWebSocketMessageDto(
    val type: IncomingWebSocketMessageType
) {
    @Serializable
    data class NewMessageDto(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String
    ): IncomingWebSocketMessageDto(
        type = IncomingWebSocketMessageType.NEW_MESSAGE
    )

    @Serializable
    data class MessageDeletedDto(
        val messageId: String,
        val chatId: String
    ): IncomingWebSocketMessageDto(
        type = IncomingWebSocketMessageType.MESSAGE_DELETED
    )

    @Serializable
    data class ProfilePicUpdatedDto(
        val userId: String,
        val newUrl: String?
    ): IncomingWebSocketMessageDto(
        type = IncomingWebSocketMessageType.PROFILE_PIC_UPDATED
    )

    @Serializable
    data class ChatParticipantsChangedDto(
        val chatId: String
    ): IncomingWebSocketMessageDto(
        type = IncomingWebSocketMessageType.CHAT_PARTICIPANTS_CHANGED
    )
}