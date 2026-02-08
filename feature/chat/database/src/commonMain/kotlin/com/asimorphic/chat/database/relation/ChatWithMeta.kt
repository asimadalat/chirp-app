package com.asimorphic.chat.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.asimorphic.chat.database.entity.ChatEntity
import com.asimorphic.chat.database.entity.ChatMessageEntity
import com.asimorphic.chat.database.entity.ChatParticipantCrossRef
import com.asimorphic.chat.database.entity.ChatParticipantEntity

data class ChatWithMeta(
    @Embedded
    val chat: ChatEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        associateBy = Junction(value = ChatParticipantCrossRef::class)
    )
    val participants: List<ChatParticipantEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "chatId",
        entity = ChatMessageEntity::class
    )
    val chatMessagesWithSenders: List<ChatMessageWithSender>
)