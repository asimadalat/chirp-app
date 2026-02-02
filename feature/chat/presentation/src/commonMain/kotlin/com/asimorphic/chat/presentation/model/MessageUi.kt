package com.asimorphic.chat.presentation.model

import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.presentation.util.UiText

sealed interface MessageUi {
    data class SelfParticipantMessage(
        val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val deliveryStatus: ChatMessageDeliveryStatus,
        val isMenuOpen: Boolean
    ): MessageUi

    data class OtherParticipantMessage(
        val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val sender: ChatParticipantUi
    ): MessageUi

    data class DateSeparator(
        val id: String,
        val date: UiText
    ): MessageUi
}