package com.asimorphic.chat.presentation.chat_menu_detail

data class ChatMenuDetailState(
    val selectedChatId: String? = null,
    val dialogState: ChatMenuDetailDialogState =
        ChatMenuDetailDialogState.Hidden
)

sealed interface ChatMenuDetailDialogState {
    data object Hidden: ChatMenuDetailDialogState
    data object CreateChat: ChatMenuDetailDialogState
    data class ManageChat(val chatId: String): ChatMenuDetailDialogState
    data object ProfileDialog: ChatMenuDetailDialogState
}
