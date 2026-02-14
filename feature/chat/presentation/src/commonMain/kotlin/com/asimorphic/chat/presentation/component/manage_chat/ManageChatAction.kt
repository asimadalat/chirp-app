package com.asimorphic.chat.presentation.component.manage_chat

sealed interface ManageChatAction {
    data object OnAddClick: ManageChatAction
    data object OnPrimaryActionClick: ManageChatAction
    data object OnDismissDialog: ManageChatAction

    sealed interface ChatParticipants: ManageChatAction{
        data class OnSelectChat(val chatId: String?): ManageChatAction
    }
}