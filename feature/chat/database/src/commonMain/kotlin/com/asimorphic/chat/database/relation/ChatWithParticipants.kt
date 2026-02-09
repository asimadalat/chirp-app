package com.asimorphic.chat.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.asimorphic.chat.database.entity.ChatEntity
import com.asimorphic.chat.database.entity.ChatParticipantCrossRef
import com.asimorphic.chat.database.entity.ChatParticipantEntity
import com.asimorphic.chat.database.view.LastMessageView

data class ChatWithParticipants(
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
        entity = LastMessageView::class
    )
    val lastMessage: LastMessageView?
)
