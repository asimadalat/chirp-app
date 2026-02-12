package com.asimorphic.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.asimorphic.chat.database.entity.ChatEntity
import com.asimorphic.chat.database.entity.ChatMessageEntity
import com.asimorphic.chat.database.entity.ChatParticipantCrossRef
import com.asimorphic.chat.database.entity.ChatParticipantEntity
import com.asimorphic.chat.database.relation.ChatWithMetaRelation
import com.asimorphic.chat.database.relation.ChatWithParticipantsRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    @Upsert
    suspend fun upsertChats(chats: List<ChatEntity>)

    @Query(value = "SELECT * FROM chat_entity WHERE chatId = :chatId")
    @Transaction
    suspend fun getChatById(chatId: String): ChatWithParticipantsRelation?

    @Query(value = """
        SELECT c.*
        FROM chat_entity c
        JOIN chat_participant_cross_ref cpcr ON c.chatId = cpcr.chatId
        WHERE c.chatId = :chatId AND cpcr.isActive = true
    """)
    @Transaction
    fun getChatWithMetaById(chatId: String): Flow<ChatWithMetaRelation?>

    @Query(value = "SELECT chatId FROM chat_entity")
    suspend fun getAllChatIds(): List<String>

    @Query(value = "SELECT * FROM chat_entity ORDER BY lastActivityAt DESC")
    @Transaction
    fun getChatsWithParticipants(): Flow<List<ChatWithParticipantsRelation>>

    @Query(value = """
        SELECT DISTINCT c.*
        FROM chat_entity c
        JOIN chat_participant_cross_ref cpcr ON c.chatId
        WHERE cpcr.isActive = true
        ORDER BY lastActivityAt DESC
    """)
    @Transaction
    fun getChatsWithActiveParticipants(): Flow<List<ChatWithParticipantsRelation>>

    @Query(value = "SELECT COUNT(*) FROM chat_entity")
    fun getChatCount(): Flow<Int>

    @Query(value = """
        SELECT p.*
        FROM chat_participant_entity p
        JOIN chat_participant_cross_ref cpcr ON p.userId = cpcr.userId
        WHERE cpcr.chatId = :chatId AND cpcr.isActive = true
        ORDER BY p.username
    """)
    fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipantEntity>>

    @Query(value = "DELETE FROM chat_entity WHERE chatId = :chatId")
    suspend fun deleteChatById(chatId: String)

    @Query(value = "DELETE FROM chat_entity WHERE chatId IN (:chatIds)")
    suspend fun deleteChatsByIds(chatIds: List<String>)

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
                chatId = chat.chatId,
                userId = it.userId,
                isActive = true
            )
        }
        crossRefDao.upsertCrossRefs(
            crossRefs = crossRefs
        )

        crossRefDao.syncChatParticipants(
            chatId = chat.chatId,
            participants = participants
        )
    }

    @Transaction
    suspend fun upsertChatsWithParticipantsAndCrossRefs(
        chats: List<ChatWithParticipantsRelation>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantCrossRefDao,
        messageDao: ChatMessageDao
    ) {
        upsertChats(
            chats = chats.map {
                it.chat
            }
        )

        val clientChatIds = getAllChatIds()
        val serverChatIds = chats.map { it.chat.chatId }
        val staleChatIds = clientChatIds - serverChatIds.toSet()

        chats.forEach { chatWithParticipants ->
            chatWithParticipants.lastMessage?.run {
                messageDao.upsertMessage(
                    message = ChatMessageEntity(
                        messageId = messageId,
                        chatId = chatId,
                        senderId = senderId,
                        content = content,
                        sentAt = sentAt,
                        deliveryStatus = deliveryStatus,
                    )
                )
            }
        }

        val allParticipantsFromChats = chats.flatMap {
            it.participants
        }
        participantDao.upsertParticipants(
            participants = allParticipantsFromChats
        )

        val allCrossRefsFromChats = chats.flatMap { chatWithParticipants ->
            chatWithParticipants.participants.map { participantEntity ->
                ChatParticipantCrossRef(
                    chatId = chatWithParticipants.chat.chatId,
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
                chatId = chatWithParticipants.chat.chatId,
                participants = chatWithParticipants.participants
            )
        }

        deleteChatsByIds(
            chatIds = staleChatIds
        )
    }
}