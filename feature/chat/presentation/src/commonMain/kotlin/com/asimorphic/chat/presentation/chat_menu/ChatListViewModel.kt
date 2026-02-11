package com.asimorphic.chat.presentation.chat_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.presentation.mapper.toUi
import com.asimorphic.core.domain.auth.SessionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatRepository: ChatRepository,
    private val sessionService: SessionService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        flow = _state,
        flow2 = chatRepository.getChats(),
        flow3 = sessionService.observeAuthCredential()
    ) { currentState, chats, authCredential ->
        if (authCredential == null)
            return@combine ChatListState()

        currentState.copy(
            chats = chats.map {
                it.toUi(selfParticipantId = authCredential.user.id)
            },
            selfParticipant = authCredential.user.toUi()
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListState()
        )

    fun onAction(action: ChatListAction) {
        when (action) {
            else -> Unit
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            chatRepository.fetchChats()
        }
    }
}