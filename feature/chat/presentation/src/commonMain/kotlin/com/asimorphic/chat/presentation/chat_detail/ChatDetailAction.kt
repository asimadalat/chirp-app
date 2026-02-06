package com.asimorphic.chat.presentation.chat_detail

import com.asimorphic.chat.presentation.model.MessageUi

sealed interface ChatDetailAction {
    data class OnSelectChat(val chatId: String): ChatDetailAction

    data class OnDeleteMessageClick(
        val message: MessageUi.SelfParticipantMessage
    ): ChatDetailAction

    data class OnMessageLongClick(
        val message: MessageUi.SelfParticipantMessage
    ): ChatDetailAction

    data class OnRetryClick(
        val message: MessageUi.SelfParticipantMessage
    ): ChatDetailAction

    data object OnDismissMessageOptions: ChatDetailAction

    data object OnBackClick: ChatDetailAction

    data object OnChatOptionsClick: ChatDetailAction

    data object OnDismissChatOptions: ChatDetailAction

    data object OnPeopleClick: ChatDetailAction

    data object OnLeaveChatClick: ChatDetailAction

    data object OnSendMessageClick: ChatDetailAction

    data object OnScrollToTop: ChatDetailAction
}