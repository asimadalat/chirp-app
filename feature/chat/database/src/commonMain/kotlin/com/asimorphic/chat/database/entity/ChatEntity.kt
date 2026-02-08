package com.asimorphic.chat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey
    val id: String,

    val lastActivityAt: Long,

    val lastMessage: String?
)
