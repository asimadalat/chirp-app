package com.asimorphic.chat.data.chat

import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.data.mapper.toEntity
import com.asimorphic.chat.data.mapper.toLastMessageView
import com.asimorphic.chat.database.ChirpChatDatabase
import com.asimorphic.chat.database.entity.ChatParticipantEntity
import com.asimorphic.chat.database.relation.ChatWithMetaRelation
import com.asimorphic.chat.database.relation.ChatWithParticipantsRelation
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.domain.chat.ChatService
import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.chat.domain.model.ChatInfo
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.onEmpty
import com.asimorphic.core.domain.util.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class OfflineFirstChatRepository(
    private val db: ChirpChatDatabase,
    private val chatService: ChatService
): ChatRepository {
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return chatService
            .createChat(
                otherUserIds = otherUserIds
            )
            .onSuccess { chat ->
                db.chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toEntity(),
                    participants = chat.participants
                        .map { it.toEntity() },
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantCrossRefDao
                )
            }
    }

    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return chatService
            .getChats()
            .onSuccess { chats ->
                val chatsWithParticipants = chats.map { chat ->
                    ChatWithParticipantsRelation(
                        chat = chat.toEntity(),
                        participants = chat.participants.map { it.toEntity() },
                        lastMessage = chat.lastMessage?.toLastMessageView()
                    )
                }

                db.chatDao.upsertChatsWithParticipantsAndCrossRefs(
                    chats = chatsWithParticipants,
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantCrossRefDao,
                    messageDao = db.chatMessageDao
                )
            }
    }

    override suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote> {
        return chatService
            .getChatById(
                chatId = chatId
            )
            .onSuccess { chat ->
                db.chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toEntity(),
                    participants = chat.participants
                        .map { it.toEntity() },
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantCrossRefDao
                )
            }
            .onEmpty()
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return chatService
            .leaveChat(
                chatId = chatId
            )
            .onSuccess {
                db.chatDao.deleteChatById(
                    chatId = chatId
                )
            }
    }

    override fun getChats(): Flow<List<Chat>> {
        return db
            .chatDao
            .getChatsWithParticipants()
            .map { chatsWithParticipants ->
                supervisorScope {
                    chatsWithParticipants
                        .map { chatWithParticipants ->
                            async {
                                ChatWithParticipantsRelation(
                                    chat = chatWithParticipants.chat,
                                    participants = chatWithParticipants
                                        .participants
                                        .filterByActive(
                                            chatId = chatWithParticipants.chat.chatId
                                        ),
                                    lastMessage = chatWithParticipants.lastMessage
                                )
                            }
                        }
                        .awaitAll()
                        .map { it.toDomain() }
                }
            }
    }

    override fun getChatInfoById(chatId: String): Flow<ChatInfo> {
        return db
            .chatDao
            .getChatWithMetaById(
                chatId = chatId
            )
            .filterNotNull()
            .map { chatWithMeta ->
                ChatWithMetaRelation(
                    chat = chatWithMeta.chat,
                    participants = chatWithMeta
                        .participants
                        .filterByActive(
                            chatId = chatWithMeta.chat.chatId
                        ),
                    chatMessagesWithSenders = chatWithMeta.chatMessagesWithSenders
                )
            }
            .map { it.toDomain() }
    }

    private suspend fun List<ChatParticipantEntity>.filterByActive(
        chatId: String
    ): List<ChatParticipantEntity> {
        val activeParticipantIds = db
            .chatDao
            .getActiveParticipantsByChatId(
                chatId = chatId
            )
            .first()
            .map { it.userId }

        return this.filter {
            it.userId in activeParticipantIds
        }
    }
}