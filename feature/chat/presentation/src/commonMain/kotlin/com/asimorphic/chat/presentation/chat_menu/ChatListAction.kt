package com.asimorphic.chat.presentation.chat_menu

sealed interface ChatListAction {
    data class OnChatClick(val chatId: String?): ChatListAction
    data object OnCreateChat: ChatListAction
    data object OnUserProfilePictureClick: ChatListAction
    data object OnManageProfileClick: ChatListAction
    data object OnLogoutClick: ChatListAction
    data object OnConfirmLogout: ChatListAction
    data object OnDismissLogoutConfirmation: ChatListAction
    data object OnDismissMenu: ChatListAction
}