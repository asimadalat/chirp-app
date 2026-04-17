package com.asimorphic.chat.data.notification

import com.asimorphic.chat.domain.chat.ChatConnectionService
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.core.domain.auth.SessionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DesktopNotifier(
    private val sessionService: SessionService,
    private val chatRepository: ChatRepository,
    private val chatConnectionService: ChatConnectionService
) {
    data class NotificationPayload(
        val title: String,
        val message: String
    )

    fun observeNewNotifications(): Flow<NotificationPayload> {
        return combine(
            sessionService.observeAuthCredential(),
            chatConnectionService.chatMessages
        ) { authCredential, chatMessage ->
            val selfParticipantId =  authCredential?.user?.id
            if (chatMessage.senderId != selfParticipantId) {
                (chatMessage to selfParticipantId)
            } else {
                null
            }
        }.filterNotNull()
            .distinctUntilChangedBy {
                (message, _) -> message.id
            }
            .map { (message, selfParticipantId) ->
                val chatInfo = chatRepository.getChatInfoById(message.chatId).firstOrNull()
                val senderName = chatInfo?.chat?.participants?.find {
                    it.userId == message.senderId
                }?.username

                val notificationTitle = chatInfo?.chat?.participants?.let { participants ->
                    participants
                        .filter { it.userId != selfParticipantId }
                        .sortedBy { it.username }
                        .joinToString(", ") { it.username }
                }

                NotificationPayload(
                    title = notificationTitle ?: "Unknown",
                    message = "$senderName: ${message.content}"
                )
            }
    }
}