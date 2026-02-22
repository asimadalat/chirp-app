package com.asimorphic.chat.presentation.chat_menu_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.chat.domain.chat.ChatConnectionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChatListDetailViewModel(
    private val chatConnectionService: ChatConnectionService
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(value = ChatListDetailState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                chatConnectionService.chatMessages.launchIn(
                    scope = viewModelScope
                )

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListDetailState()
        )

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