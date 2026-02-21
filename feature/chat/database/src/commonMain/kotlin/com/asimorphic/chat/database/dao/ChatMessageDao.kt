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

    @Query("SELECT * FROM chat_message_entity WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): ChatMessageEntity?

    @Query(value = """
        SELECT * FROM chat_message_entity 
        WHERE chatId = :chatId 
        ORDER BY sentAt DESC
    """)
    fun getMessagesByChatId(chatId: String): Flow<List<ChatMessageEntity>>

    @Query(value = """
        UPDATE chat_message_entity
        SET deliveryStatus = :status, receivedAt = :receivedAt 
        WHERE messageId = :messageId
    """)
    suspend fun updateDeliveryStatus(messageId: String, status: String, receivedAt: Long)

    @Query(value = "DELETE FROM chat_message_entity WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query(value = "DELETE FROM chat_message_entity WHERE messageId IN (:messageIds)")
    suspend fun deleteMessagesByIds(messageIds: List<String>)
}