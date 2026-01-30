package com.asimorphic.chat.presentation.create_chat

sealed interface CreateChatAction {
    data object OnAddClick: CreateChatAction
    data object OnCreateChatClick: CreateChatAction
    data object OnDismissDialog: CreateChatAction
}