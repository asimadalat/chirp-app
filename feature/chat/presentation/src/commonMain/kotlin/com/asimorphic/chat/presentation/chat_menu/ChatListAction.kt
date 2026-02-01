package com.asimorphic.chat.presentation.chat_menu

import com.asimorphic.chat.presentation.model.ChatUi

sealed interface ChatListAction {
    data class OnChatClick(val chat: ChatUi): ChatListAction
    data object OnCreateChat: ChatListAction
    data object OnUserProfilePictureClick: ChatListAction
    data object OnManageProfileClick: ChatListAction
    data object OnLogoutClick: ChatListAction
    data object OnConfirmLogout: ChatListAction
    data object OnDismissLogoutConfirmation: ChatListAction
    data object OnDismissMenu: ChatListAction
}