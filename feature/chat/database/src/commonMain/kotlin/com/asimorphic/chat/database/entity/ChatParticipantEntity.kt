package com.asimorphic.chat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_participant_entity"
)
data class ChatParticipantEntity(
    @PrimaryKey
    val userId: String,

    val username: String,

    val profilePictureUrl: String?
)
