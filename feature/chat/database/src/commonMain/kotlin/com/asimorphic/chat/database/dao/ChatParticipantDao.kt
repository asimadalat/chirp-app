package com.asimorphic.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.asimorphic.chat.database.entity.ChatParticipantEntity

@Dao
interface ChatParticipantDao {
    @Upsert
    suspend fun upsertParticipant(participant: ChatParticipantEntity)

    @Upsert
    suspend fun upsertParticipants(participants: List<ChatParticipantEntity>)

    @Query(value = "SELECT * FROM chat_participant_entity")
    suspend fun getAllParticipants(): List<ChatParticipantEntity>
}