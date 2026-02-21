package com.asimorphic.chat.data.chat_message

import com.asimorphic.chat.database.ChirpChatDatabase
import com.asimorphic.chat.domain.chat_message.ChatMessageRepository
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.core.data.database.executeDatabaseUpdate
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import kotlin.time.Clock

class OfflineFirstChatMessageRepository(
    private val db: ChirpChatDatabase
): ChatMessageRepository {
    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return executeDatabaseUpdate {
            db.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                receivedAt = Clock.System.now().toEpochMilliseconds()
            )
        }
    }
}