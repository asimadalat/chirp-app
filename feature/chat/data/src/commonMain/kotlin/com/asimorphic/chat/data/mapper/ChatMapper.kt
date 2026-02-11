package com.asimorphic.chat.data.mapper

import com.asimorphic.chat.data.dto.ChatDto
import com.asimorphic.chat.database.entity.ChatEntity
import com.asimorphic.chat.database.relation.ChatMessageWithSenderRelation
import com.asimorphic.chat.database.relation.ChatWithMetaRelation
import com.asimorphic.chat.database.relation.ChatWithParticipantsRelation
import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.chat.domain.model.ChatInfo
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.chat.domain.model.ChatParticipant
import com.asimorphic.chat.domain.model.ChatMessageWithSender
import kotlin.time.Instant

fun ChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.parse(input = lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}

fun ChatEntity.toDomain(
    participants: List<ChatParticipant>,
    lastMessage: ChatMessage? = null
): Chat {
    return Chat(
        id = chatId,
        participants = participants,
        lastActivityAt = Instant.fromEpochMilliseconds(epochMilliseconds = lastActivityAt),
        lastMessage = lastMessage
    )
}

fun ChatWithParticipantsRelation.toDomain(): Chat {
    return Chat(
        id = chat.chatId,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant
            .fromEpochMilliseconds(
                epochMilliseconds = chat.lastActivityAt
            ),
        lastMessage = lastMessage?.toDomain()
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        lastActivityAt = lastActivityAt.toEpochMilliseconds()
    )
}

fun ChatWithMetaRelation.toDomain(): ChatInfo {
    return ChatInfo(
        chat = chat.toDomain(
            participants = this.participants.map { it.toDomain() }
        ),
        messages = chatMessagesWithSenders.map { it.toDomain() }
    )
}

fun ChatMessageWithSenderRelation.toDomain(): ChatMessageWithSender {
    return ChatMessageWithSender(
        sender = sender.toDomain(),
        message = message.toDomain(),
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(
            value = this.message.deliveryStatus
        )
    )
}