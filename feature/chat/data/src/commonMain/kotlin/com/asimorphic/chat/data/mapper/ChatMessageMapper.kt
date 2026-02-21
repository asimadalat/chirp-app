package com.asimorphic.chat.data.mapper

import com.asimorphic.chat.data.dto.ChatMessageDto
import com.asimorphic.chat.data.dto.websocket.IncomingWebSocketMessageDto
import com.asimorphic.chat.data.dto.websocket.OutgoingWebSocketMessageDto
import com.asimorphic.chat.database.entity.ChatMessageEntity
import com.asimorphic.chat.database.view.LastMessageView
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import kotlin.time.Instant

fun ChatMessageDto.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = Instant.parse(input = createdAt),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}

fun ChatMessageEntity.toDomain(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(epochMilliseconds = sentAt),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}

fun LastMessageView.toDomain(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        senderId = senderId,
        content = content,
        createdAt = Instant
            .fromEpochMilliseconds(
                epochMilliseconds = sentAt
            ),
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(value = this.deliveryStatus)
    )
}

fun ChatMessage.toLastMessageView(): LastMessageView {
    return LastMessageView(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        sentAt = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name,
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        sentAt = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name,
    )
}

fun ChatMessage.toOutgoingNewMessageDto(): OutgoingWebSocketMessageDto.NewMessage {
    return OutgoingWebSocketMessageDto.NewMessage(
        messageId = id,
        chatId = chatId,
        content = content
    )
}

fun IncomingWebSocketMessageDto.NewMessageDto.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        sentAt = Instant.parse(input = createdAt).toEpochMilliseconds(),
        deliveryStatus = ChatMessageDeliveryStatus.SENT.name,
    )
}