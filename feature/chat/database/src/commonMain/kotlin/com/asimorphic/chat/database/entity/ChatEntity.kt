package com.asimorphic.chat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_entity"
)
data class ChatEntity(
    @PrimaryKey
    val chatId: String,

    val lastActivityAt: Long,
)
