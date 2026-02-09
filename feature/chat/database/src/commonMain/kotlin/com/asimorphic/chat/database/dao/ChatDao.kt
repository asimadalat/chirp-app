package com.asimorphic.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.asimorphic.chat.database.entity.ChatEntity
import com.asimorphic.chat.database.entity.ChatParticipantCrossRef
import com.asimorphic.chat.database.entity.ChatParticipantEntity
import com.asimorphic.chat.database.relation.ChatWithMeta
import com.asimorphic.chat.database.relation.ChatWithParticipants
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    @Upsert
    suspend fun upsertChats(chats: List<ChatEntity>)

    @Query(value = "SELECT * FROM chat_entity WHERE id = :id")
    suspend fun getChatById(id: String): ChatWithParticipants?

    @Query(value = "SELECT * FROM chat_entity WHERE id = :id")
    suspend fun getChatWithMetaById(id: String): Flow<ChatWithMeta?>

    @Query(value = "SELECT id FROM chat_entity")
    fun getAllChatIds(): List<String>

    @Query(value = "SELECT * FROM chat_entity ORDER BY lastActivityAt DESC")
    fun getChatsWithParticipants(): Flow<List<ChatWithParticipants>>

    @Query(value = "SELECT COUNT(*) FROM chat_entity")
    fun getChatCount(): Flow<Int>

    @Query(value = """
        SELECT p.*
        FROM chat_participant_entity p
        JOIN chat_participant_cross_ref cpcr ON p.userId = cpcr.userId
        WHERE cpcr.chatId = :id AND cpcr.isActive = true
        ORDER BY p.username
    """)
    fun getActiveParticipantsByChatId(id: String): Flow<List<ChatParticipantEntity>>

    @Query(value = "DELETE FROM chat_entity WHERE id = :id")
    suspend fun deleteChatById(id: String)

    @Query(value = "DELETE FROM chat_entity WHERE id IN (:ids)")
    suspend fun deleteChatsByIds(ids: List<String>)

    @Query(value = "DELETE FROM chat_entity")
    suspend fun deleteAllChats()

    @Transaction
    suspend fun upsertChatWithParticipantsAndCrossRefs(
        chat: ChatEntity,
        participants: List<ChatParticipantEntity>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantCrossRefDao
    ) {
        upsertChat(
            chat = chat
        )
        participantDao.upsertParticipants(
            participants = participants
        )

        val crossRefs = participants.map {
            ChatParticipantCrossRef(
                chatId = chat.id,
                userId = it.userId,
                isActive = true
            )
        }
        crossRefDao.upsertCrossRefs(
            crossRefs = crossRefs
        )

        crossRefDao.syncChatParticipants(
            chatId = chat.id,
            participants = participants
        )
    }

    @Transaction
    suspend fun upsertChatsWithParticipantsAndCrossRefs(
        chats: List<ChatWithParticipants>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantCrossRefDao
    ) {
        upsertChats(
            chats = chats.map {
                it.chat
            }
        )

        val allParticipantsFromChats = chats.flatMap {
            it.participants
        }
        participantDao.upsertParticipants(
            participants = allParticipantsFromChats
        )

        val allCrossRefsFromChats = chats.flatMap { chatWithParticipants ->
            chatWithParticipants.participants.map { participantEntity ->
                ChatParticipantCrossRef(
                    chatId = chatWithParticipants.chat.id,
                    userId = participantEntity.userId,
                    isActive = true
                )
            }
        }
        crossRefDao.upsertCrossRefs(
            crossRefs = allCrossRefsFromChats
        )

        chats.forEach { chatWithParticipants ->
            crossRefDao.syncChatParticipants(
                chatId = chatWithParticipants.chat.id,
                participants = chatWithParticipants.participants
            )
        }
    }
}