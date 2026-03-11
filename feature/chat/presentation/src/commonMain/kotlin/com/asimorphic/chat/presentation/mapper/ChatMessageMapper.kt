package com.asimorphic.chat.presentation.mapper

import com.asimorphic.chat.domain.model.ChatMessageWithSender
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.chat.presentation.util.DateTimeFormatter
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun ChatMessageWithSender.toUi(selfUserId: String): MessageUi {
    return if (this.sender.userId == selfUserId) {
        MessageUi.SelfParticipantMessage(
            id = message.id,
            content = message.content,
            formattedSentTime = DateTimeFormatter.formatMessageTime(
                instant = message.createdAt
            ),
            deliveryStatus = message.deliveryStatus,
        )
    } else {
        MessageUi.OtherParticipantMessage(
            id = message.id,
            content = message.content,
            formattedSentTime = DateTimeFormatter.formatMessageTime(
                instant = message.createdAt
            ),
            sender = sender.toUi()
        )
    }
}

fun List<ChatMessageWithSender>.toUiList(selfUserId: String): List<MessageUi> {
    return this
        .sortedByDescending {
            it.message.createdAt
        }
        .groupBy {
            it.message.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
        .flatMap { (date, messages) ->
            messages.map { it.toUi(selfUserId) } + MessageUi.DateSeparator(
                id = date.toString(),
                date = DateTimeFormatter.formatDateSeparator(date)
            )
        }
}