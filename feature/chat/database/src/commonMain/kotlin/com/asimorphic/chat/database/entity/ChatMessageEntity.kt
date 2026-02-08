package com.asimorphic.chat.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_message_entity",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChatMessageEntity(
    @PrimaryKey
    val id: String,

    val chatId: String,

    val senderId: String,

    val content: String,

    val sentAt: Long,

    val deliveryStatus: String,

    val receivedAt: Long = sentAt
)
