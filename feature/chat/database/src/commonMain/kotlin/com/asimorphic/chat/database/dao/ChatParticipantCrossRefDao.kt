package com.asimorphic.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.asimorphic.chat.database.entity.ChatParticipantCrossRef
import com.asimorphic.chat.database.entity.ChatParticipantEntity

@Dao
interface ChatParticipantCrossRefDao {
    @Upsert
    suspend fun upsertCrossRefs(crossRefs: List<ChatParticipantCrossRef>)

    @Query(value = """
        SELECT userId FROM chat_participant_cross_ref 
        WHERE chatId == chatId
    """)
    suspend fun getAllParticipantIdsByChatId(chatId: String): List<String>

    @Query(value = """
        SELECT userId FROM chat_participant_cross_ref 
        WHERE chatId == chatId AND isActive == true
    """)
    suspend fun getActiveParticipantIdsByChatId(chatId: String): List<String>

    @Query(value = """
        UPDATE chat_participant_cross_ref
        SET isActive = false
        WHERE chatId = :chatId AND userId IN (:userIds)
    """)
    suspend fun deactivateParticipants(chatId: String, userIds: List<String>)

    @Query(value = """
        UPDATE chat_participant_cross_ref
        SET isActive = true
        WHERE chatId = :chatId AND userId IN (:userIds)
    """)
    suspend fun reactivateParticipants(chatId: String, userIds: List<String>)

    @Transaction
    suspend fun syncChatParticipants(
        chatId: String,
        participants: List<ChatParticipantEntity>
    ) {
        if (participants.isEmpty())
            return

        val serverParticipantIds = participants.map {
            it.userId
        }.toSet()

        val allClientParticipantIds = getAllParticipantIdsByChatId(chatId = chatId).toSet()
        val activeClientParticipants = getActiveParticipantIdsByChatId(chatId = chatId).toSet()
        val inactiveClientParticipantIds = allClientParticipantIds - activeClientParticipants

        val participantsToReactivate = serverParticipantIds.intersect(other = inactiveClientParticipantIds)
        val participantsToDeactivate = activeClientParticipants - serverParticipantIds

        deactivateParticipants(
            chatId = chatId,
            userIds = participantsToDeactivate.toList()
        )

        reactivateParticipants(
            chatId = chatId,
            userIds = participantsToReactivate.toList()
        )

        val newParticipantIds = serverParticipantIds - allClientParticipantIds
        val newCrossRefs = newParticipantIds.map { userId ->
            ChatParticipantCrossRef(
                chatId = chatId,
                userId = userId,
                isActive = true
            )
        }

        upsertCrossRefs(newCrossRefs)
    }
}