package com.asimorphic.chat.data.chat

import com.asimorphic.chat.data.dto.websocket.IncomingWebSocketMessageDto
import com.asimorphic.chat.data.dto.websocket.IncomingWebSocketMessageType
import com.asimorphic.chat.data.dto.websocket.WebSocketMessageDto
import com.asimorphic.chat.data.mapper.toDomain
import com.asimorphic.chat.data.mapper.toEntity
import com.asimorphic.chat.data.mapper.toOutgoingNewMessageDto
import com.asimorphic.chat.data.network.KtorWebSocketConnector
import com.asimorphic.chat.database.ChirpChatDatabase
import com.asimorphic.chat.domain.chat.ChatConnectionService
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.domain.chat_message.ChatMessageRepository
import com.asimorphic.chat.domain.exception.ConnectionError
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.onFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class ChatWebSocketConnectionService(
    private val webSocketConnector: KtorWebSocketConnector,
    private val db: ChirpChatDatabase,
    private val chatRepository: ChatRepository,
    private val sessionService: SessionService,
    private val applicationScope: CoroutineScope,
    private val json: Json,
    private val chatMessageRepository: ChatMessageRepository
): ChatConnectionService {
    override val chatMessages: Flow<ChatMessage> = webSocketConnector
        .messages
        .mapNotNull { parseIncomingMessage(message = it) }
        .onEach { handleIncomingMessage(message = it) }
        .filterIsInstance<IncomingWebSocketMessageDto.NewMessageDto>()
        .mapNotNull {
            db.chatMessageDao.getMessageById(
                messageId = it.id
            )?.toDomain()
        }
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L)
        )

    override val connectionState = webSocketConnector.connectionState

    override suspend fun sendChatMessage(message: ChatMessage): EmptyResult<ConnectionError> {
        val outgoingNewMessage = message.toOutgoingNewMessageDto()
        val webSocketMessage = WebSocketMessageDto(
            type = outgoingNewMessage.type.name,
            payload = json.encodeToString(value = outgoingNewMessage)
        )

        val rawSerializedMessage = json.encodeToString(value = webSocketMessage)

        return webSocketConnector
            .sendMessage(
                message = rawSerializedMessage
            )
            .onFailure { exception ->
                chatMessageRepository.updateMessageDeliveryStatus(
                    messageId = message.id,
                    status = ChatMessageDeliveryStatus.FAILED
                )
            }
    }

    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingWebSocketMessageDto? {
        return when (message.type) {
            IncomingWebSocketMessageType.NEW_MESSAGE.name -> {
                json.decodeFromString<IncomingWebSocketMessageDto.NewMessageDto>(
                    string = message.payload
                )
            }
            IncomingWebSocketMessageType.MESSAGE_DELETED.name -> {
                json.decodeFromString<IncomingWebSocketMessageDto.MessageDeletedDto>(
                    string = message.payload
                )
            }
            IncomingWebSocketMessageType.PROFILE_PIC_UPDATED.name -> {
                json.decodeFromString<IncomingWebSocketMessageDto.ProfilePicUpdatedDto>(
                    string = message.payload
                )
            }
            IncomingWebSocketMessageType.CHAT_PARTICIPANTS_CHANGED.name -> {
                json.decodeFromString<IncomingWebSocketMessageDto.ChatParticipantsChangedDto>(
                    string = message.payload
                )
            }
            else -> null
        }
    }

    private suspend fun handleIncomingMessage(message: IncomingWebSocketMessageDto) {
        when (message) {
            is IncomingWebSocketMessageDto.NewMessageDto -> handleNewMessage(message)
            is IncomingWebSocketMessageDto.MessageDeletedDto -> handleMessageDelete(message)
            is IncomingWebSocketMessageDto.ProfilePicUpdatedDto -> handleProfilePictureUpdate(message)
            is IncomingWebSocketMessageDto.ChatParticipantsChangedDto -> handleRefreshChat(message)
        }
    }

    private suspend fun handleProfilePictureUpdate(message: IncomingWebSocketMessageDto.ProfilePicUpdatedDto) {
        db.chatParticipantDao.updateProfilePictureUrl(
            userId = message.userId,
            newUrl = message.newUrl
        )

        val authCredential = sessionService.observeAuthCredential().firstOrNull()
        if (authCredential != null && message.userId == authCredential.user.id) {
            sessionService.set(
                credential = authCredential.copy(
                    user = authCredential.user.copy(
                        profilePictureUrl = message.newUrl
                    )
                )
            )
        }
    }

    private suspend fun handleNewMessage(message: IncomingWebSocketMessageDto.NewMessageDto) {
        val doesChatExist = db.chatDao.getChatById(
            chatId = message.chatId
        ) != null

        if (!doesChatExist)
            chatRepository.fetchChatById(
                chatId = message.chatId
            )

        db.chatMessageDao.upsertMessage(
            message = message.toEntity()
        )
    }

    private suspend fun handleMessageDelete(message: IncomingWebSocketMessageDto.MessageDeletedDto) {
        db.chatMessageDao.deleteMessageById(
            messageId = message.messageId
        )
    }

    private suspend fun handleRefreshChat(
        message: IncomingWebSocketMessageDto.ChatParticipantsChangedDto
    ) {
        chatRepository.fetchChatById(
            chatId = message.chatId
        )
    }
}