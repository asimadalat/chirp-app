package com.asimorphic.chat.data.chat_message

import com.asimorphic.chat.data.dto.websocket.OutgoingWebSocketMessageDto
import com.asimorphic.chat.data.dto.websocket.OutgoingWebSocketMessageType
import com.asimorphic.chat.data.dto.websocket.WebSocketMessageDto
import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.data.mapper.toEntity
import com.asimorphic.chat.data.mapper.toWebSocketDto
import com.asimorphic.chat.data.network.KtorWebSocketConnector
import com.asimorphic.chat.database.ChirpChatDatabase
import com.asimorphic.chat.domain.chat_message.ChatMessageRepository
import com.asimorphic.chat.domain.chat_message.ChatMessageService
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.chat.domain.model.ChatMessageWithSender
import com.asimorphic.chat.domain.model.OutgoingNewMessage
import com.asimorphic.core.data.database.executeDatabaseUpdate
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class OfflineFirstChatMessageRepository(
    private val db: ChirpChatDatabase,
    private val chatMessageService: ChatMessageService,
    private val webSocketConnector: KtorWebSocketConnector,
    private val sessionService: SessionService,
    private val json: Json,
    private val applicationScope: CoroutineScope
): ChatMessageRepository {
    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return executeDatabaseUpdate {
            val dto = message.toWebSocketDto()
            val selfUser = sessionService.observeAuthCredential().first()?.user
                ?: return Result.Failure(error = DataError.Local.NOT_FOUND)

            db.chatMessageDao.upsertMessage(
                message = dto.toEntity(
                    senderId = selfUser.id,
                    deliveryStatus = ChatMessageDeliveryStatus.SENDING
                )
            )

            return webSocketConnector
                .sendMessage(
                    message = dto.toJson()
                )
                .onFailure { exception ->
                    applicationScope.launch {
                        db.chatMessageDao.updateDeliveryStatus(
                            messageId = message.messageId,
                            status = ChatMessageDeliveryStatus.FAILED.name,
                            sentAt = Clock.System.now().toEpochMilliseconds()
                        )
                    }.join()
                }
        }
    }

    override suspend fun retryMessage(messageId: String): EmptyResult<DataError> {
        return executeDatabaseUpdate {
            val message = db
                .chatMessageDao
                .getMessageById(
                    messageId = messageId
                )
                ?: return Result.Failure(
                    error = DataError.Local.NOT_FOUND
                )

            db.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = ChatMessageDeliveryStatus.SENDING.name,
                sentAt = Clock.System.now().toEpochMilliseconds()
            )

            val dto = OutgoingWebSocketMessageDto.NewMessage(
                chatId = message.chatId,
                messageId = messageId,
                content = message.content,
                type = OutgoingWebSocketMessageType.NEW_MESSAGE
            )

            return webSocketConnector
                .sendMessage(
                    message = dto.toJson()
                )
                .onFailure { exception ->
                    applicationScope.launch {
                        db.chatMessageDao.updateDeliveryStatus(
                            messageId = messageId,
                            status = ChatMessageDeliveryStatus.FAILED.name,
                            sentAt = Clock.System.now().toEpochMilliseconds()
                        )
                    }.join()
                }
        }
    }

    override suspend fun getMessagesForChat(chatId: String): Flow<List<ChatMessageWithSender>> {
        return db.chatMessageDao
            .getMessagesByChatId(
                chatId = chatId
            )
            .map { messageEntities ->
                messageEntities.map { it.toDomain() }
            }
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(
                chatId = chatId,
                before = before
            )
            .onSuccess { messages ->
                return executeDatabaseUpdate {
                    db.chatMessageDao.upsertMessagesAndSyncIfApplicable(
                        chatId = chatId,
                        serverMessages = messages
                            .map {
                                it.toEntity()
                            },
                        pageSize = ChatMessageConstants.PAGE_SIZE,
                        shouldSync = before == null
                    )
                    messages
                }
            }
    }

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return executeDatabaseUpdate {
            db.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                sentAt = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote> {
        return chatMessageService
            .deleteMessage(
                messageId = messageId
            )
            .onSuccess {
                applicationScope.launch {
                    db.chatMessageDao.deleteMessageById(
                        messageId = messageId
                    )
                }.join()
            }
    }

    private fun OutgoingWebSocketMessageDto.NewMessage.toJson(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(value = this)
        )

        return json.encodeToString(value = webSocketMessage)
    }
}