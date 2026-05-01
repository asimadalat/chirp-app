package com.asimorphic.chat.database.view

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "last_message_view_per_chat",
    value = """
        SELECT m1.*, p.username AS senderUsername
        FROM chat_message_entity m1
        JOIN (
            SELECT chatId, MAX(sentAt) AS max_sent_at
            FROM chat_message_entity
            GROUP BY chatId
        ) m2 ON m1.chatId = m2.chatId AND m1.sentAt = m2.max_sent_at
        LEFT JOIN chat_participant_entity p ON m1.senderId = p.userId
    """
)
data class LastMessageView(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val sentAt: Long,
    val deliveryStatus: String,
    val senderUsername: String?
)
