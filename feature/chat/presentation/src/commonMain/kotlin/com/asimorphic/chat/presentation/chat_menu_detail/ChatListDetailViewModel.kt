package com.asimorphic.chat.presentation.chat_menu_detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatListDetailViewModel: ViewModel() {

    private val _state = MutableStateFlow(value = ChatMenuDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: ChatListDetailAction) {
        when (action) {
            is ChatListDetailAction.OnChatClick -> {
                _state.update { it.copy(
                    selectedChatId = action.chatId
                ) }
            }
            ChatListDetailAction.OnCreateChatClick -> {
                _state.update { it.copy(
                    dialogState = ChatMenuDetailDialogState.CreateChat
                ) }
            }
            ChatListDetailAction.OnDismissCurrentDialog -> {
                _state.update { it.copy(
                    dialogState = ChatMenuDetailDialogState.Hidden
                ) }
            }
            ChatListDetailAction.OnManageChatClick -> {
                state.value.selectedChatId?.let { id ->
                    _state.update { it.copy(
                        dialogState = ChatMenuDetailDialogState.ManageChat(chatId = id)
                    ) }
                }
            }
            ChatListDetailAction.OnManageProfileClick -> {
                _state.update { it.copy(
                    dialogState = ChatMenuDetailDialogState.ProfileDialog
                ) }
            }
        }
    }
}