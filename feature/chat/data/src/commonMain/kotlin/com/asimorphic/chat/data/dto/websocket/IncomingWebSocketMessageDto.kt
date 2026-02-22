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
sealed interface IncomingWebSocketMessageDto {
    @Serializable
    data class NewMessageDto(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String,
        val type: IncomingWebSocketMessageType = IncomingWebSocketMessageType.NEW_MESSAGE
    ): IncomingWebSocketMessageDto

    @Serializable
    data class MessageDeletedDto(
        val messageId: String,
        val chatId: String,
        val type: IncomingWebSocketMessageType = IncomingWebSocketMessageType.MESSAGE_DELETED
    ): IncomingWebSocketMessageDto

    @Serializable
    data class ProfilePicUpdatedDto(
        val userId: String,
        val newUrl: String?,
        val type: IncomingWebSocketMessageType = IncomingWebSocketMessageType.PROFILE_PIC_UPDATED
    ): IncomingWebSocketMessageDto

    @Serializable
    data class ChatParticipantsChangedDto(
        val chatId: String,
        val type: IncomingWebSocketMessageType = IncomingWebSocketMessageType.CHAT_PARTICIPANTS_CHANGED
    ): IncomingWebSocketMessageDto
}