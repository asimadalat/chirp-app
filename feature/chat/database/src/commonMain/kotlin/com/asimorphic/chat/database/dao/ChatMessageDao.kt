package com.asimorphic.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.asimorphic.chat.database.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Upsert
    suspend fun upsertMessage(message: ChatMessageEntity)

    @Upsert
    suspend fun upsertMessages(messages: List<ChatMessageEntity>)

    @Query("SELECT * FROM chat_message_entity WHERE id = :id")
    suspend fun getMessageById(id: String): ChatMessageEntity?

    @Query(value = """
        SELECT * FROM chat_message_entity 
        WHERE chatId = :chatId 
        ORDER BY sentAt DESC
    """)
    fun getMessagesByChatId(chatId: String): Flow<List<ChatMessageEntity>>

    @Query(value = "DELETE FROM chat_message_entity WHERE id = :id")
    suspend fun deleteMessageById(id: String)

    @Query(value = "DELETE FROM chat_message_entity WHERE id IN (:ids)")
    suspend fun deleteMessagesByIds(ids: List<String>)
}