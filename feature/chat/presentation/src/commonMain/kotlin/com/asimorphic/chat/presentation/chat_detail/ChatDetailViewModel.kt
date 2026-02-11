@file:OptIn(ExperimentalCoroutinesApi::class)

package com.asimorphic.chat.presentation.chat_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.presentation.mapper.toUi
import com.asimorphic.core.domain.auth.SessionService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatDetailViewModel(
    private val chatRepository: ChatRepository,
    private val sessionService: SessionService
) : ViewModel() {

    private val _chatId = MutableStateFlow<String?>(value = null)

    private var hasLoadedInitialData = false

    private val chatInfoFlow = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null)
                chatRepository.getChatInfoById(chatId = chatId)
            else
                emptyFlow()
        }

    private val _state = MutableStateFlow(ChatDetailState())

    private val stateWithMessages = combine(
        flow = _state,
        flow2 = chatInfoFlow,
        flow3 = sessionService.observeAuthCredential()
    ) { currentState, chatInfo, authCredential ->
        if (authCredential == null)
            return@combine ChatDetailState()

        currentState.copy(
            chatUi = chatInfo.chat.toUi(
                selfParticipantId = authCredential.user.id
            )
        )
    }

    val state = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null)
                stateWithMessages
            else
                _state
        }
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatDetailState()
        )

    fun onAction(action: ChatDetailAction) {
        when (action) {
            is ChatDetailAction.OnSelectChat -> switchChat(chatId = action.chatId)
            else -> Unit
        }
    }

    private fun switchChat(chatId: String?) {
        _chatId.update { chatId }
        viewModelScope.launch {
            chatId?.let {
                chatRepository.fetchChatById(
                    chatId = chatId
                )
            }
        }
    }
}