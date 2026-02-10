package com.asimorphic.chat.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_message_entity",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["chatId"]),
        Index(value = ["sentAt"])
    ]
)
data class ChatMessageEntity(
    @PrimaryKey
    val messageId: String,

    val chatId: String,

    val senderId: String,

    val content: String,

    val sentAt: Long,

    val deliveryStatus: String,

    val receivedAt: Long = sentAt
)
