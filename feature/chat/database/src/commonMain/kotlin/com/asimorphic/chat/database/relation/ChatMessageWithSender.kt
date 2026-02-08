package com.asimorphic.chat.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.asimorphic.chat.database.entity.ChatMessageEntity
import com.asimorphic.chat.database.entity.ChatParticipantEntity

data class ChatMessageWithSender(
    @Embedded
    val message: ChatMessageEntity,

    @Relation(
        parentColumn = "senderId",
        entityColumn = "userId"
    )
    val sender: ChatParticipantEntity
)