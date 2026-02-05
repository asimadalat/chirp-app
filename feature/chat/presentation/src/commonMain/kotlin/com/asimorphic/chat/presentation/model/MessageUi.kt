package com.asimorphic.chat.presentation.model

import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.presentation.util.UiText

sealed class MessageUi(open val id: String) {
    data class SelfParticipantMessage(
        override val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val deliveryStatus: ChatMessageDeliveryStatus,
        val isMenuOpen: Boolean
    ): MessageUi(id)

    data class OtherParticipantMessage(
        override val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val sender: ChatParticipantUi
    ): MessageUi(id)

    data class DateSeparator(
        override val id: String,
        val date: UiText
    ): MessageUi(id)
}