package com.asimorphic.chat.presentation.chat_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.domain.chat_participant.ChatParticipantRepository
import com.asimorphic.chat.domain.notification.DeviceTokenService
import com.asimorphic.chat.presentation.mapper.toUi
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import com.asimorphic.core.presentation.mapper.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val authService: AuthService,
    private val chatRepository: ChatRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val sessionService: SessionService,
    private val deviceTokenService: DeviceTokenService
) : ViewModel() {

    private val eventChannel = Channel<ChatListEvent>()
    val events = eventChannel.receiveAsFlow()

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
                fetchSelfParticipant()
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
            is ChatListAction.OnChatClick -> selectChat(action.chatId)
            ChatListAction.OnUserProfilePictureClick -> showProfileMenu()
            ChatListAction.OnLogoutClick -> showLogoutConfirmation()
            ChatListAction.OnConfirmLogout -> logout()
            ChatListAction.OnDismissLogoutConfirmation -> dismissLogoutConfirmation()
            ChatListAction.OnManageProfileClick,
            ChatListAction.OnDismissMenu -> dismissProfileMenu()
            else -> Unit
        }
    }

    private fun fetchSelfParticipant() {
        viewModelScope.launch {
            chatParticipantRepository.fetchSelfParticipant()
        }
    }

    private fun selectChat(chatId: String?) {
        _state.update { it.copy(
            selectedChatId = chatId
        ) }
    }

    private fun showProfileMenu() {
        _state.update { it.copy(
            isMenuOpen = true
        ) }
    }

    private fun dismissProfileMenu() {
        _state.update { it.copy(
            isMenuOpen = false
        ) }
    }

    private fun dismissLogoutConfirmation() {
        _state.update { it.copy(
            showLogoutConfirmation = false
        ) }
    }

    private fun logout() {
        _state.update { it.copy(
            showLogoutConfirmation = false
        ) }

        viewModelScope.launch {
            val authCredential = sessionService.observeAuthCredential().first()
            val refreshToken = authCredential?.refreshToken ?: return@launch

            deviceTokenService.deregisterToken(refreshToken)
                .onSuccess {
                    authService.logout(refreshToken)
                        .onSuccess {
                            sessionService.set(null)
                            chatRepository.deleteAllChats()
                            eventChannel.send(ChatListEvent.OnLogoutSuccess)
                        }
                        .onFailure { exception ->
                            eventChannel.send(ChatListEvent.OnLogoutError(error = exception.toUiText()))
                        }
                }
                .onFailure { exception ->
                    eventChannel.send(ChatListEvent.OnLogoutError(error = exception.toUiText()))
                }
        }
    }

    private fun showLogoutConfirmation() {
        _state.update { it.copy(
            isMenuOpen = false,
            showLogoutConfirmation = true
        ) }
    }

    private fun loadChats() {
        viewModelScope.launch {
            chatRepository.fetchChats()
        }
    }
}