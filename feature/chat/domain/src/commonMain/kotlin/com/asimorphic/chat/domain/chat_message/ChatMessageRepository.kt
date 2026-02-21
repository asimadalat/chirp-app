package com.asimorphic.chat.domain.chat_message

import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult

interface ChatMessageRepository {
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local>
}