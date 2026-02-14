@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.asimorphic.chat.presentation.manage_chat

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.error_participant_not_found
import com.asimorphic.chat.domain.chat.ChatParticipantService
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.presentation.component.manage_chat.ManageChatAction
import com.asimorphic.chat.presentation.component.manage_chat.ManageChatState
import com.asimorphic.chat.presentation.mapper.toUi
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import com.asimorphic.core.presentation.mapper.toUiText
import com.asimorphic.core.presentation.util.UiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ManageChatViewModel(
    private val chatParticipantService: ChatParticipantService,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _chatId = MutableStateFlow<String?>(value = null)

    private val eventChannel = Channel<ManageChatEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ManageChatState())

    private val searchFlow = snapshotFlow { _state.value.queryTextFieldState.text.toString() }
        .debounce(timeout = 1.seconds)
        .onEach { query ->
            searchParticipant(query)
        }

    val state = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null)
                chatRepository.getActiveChatParticipantsByChatId(chatId = chatId)
            else
                emptyFlow()
        }
        .combine(flow = _state) { participants, currentState ->
            currentState.copy(
                existingChatParticipants = participants.map { it.toUi() }
            )
        }
        .onStart {
            if (!hasLoadedInitialData) {
                searchFlow.launchIn(scope = viewModelScope)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ManageChatState()
        )

    fun onAction(action: ManageChatAction) {
        when (action) {
            ManageChatAction.OnAddClick -> addParticipantToSelected()
            ManageChatAction.OnPrimaryActionClick -> addParticipantsToChat()
            is ManageChatAction.ChatParticipants.OnSelectChat -> {
                _chatId.update {
                    action.chatId
                }
            }
            else -> Unit
        }
    }

    private fun searchParticipant(query: String) {
        if (query.isBlank()) {
            _state.update { it.copy(
                canAddParticipant = false,
                currentSearchResult = null,
                searchError = null
            ) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(
                isSearchingParticipants = true,
                canAddParticipant = false
            ) }

            chatParticipantService
                .searchParticipant(query = query)
                .onSuccess { chatParticipant ->
                    _state.update { it.copy(
                        currentSearchResult = chatParticipant.toUi(),
                        canAddParticipant = true,
                        isSearchingParticipants = false,
                        searchError = null
                    ) }
                }
                .onFailure { dataErrorRemote ->
                    val errorMessage = when (dataErrorRemote) {
                        DataError.Remote.NOT_FOUND -> UiText.Resource(
                            id = Res.string.error_participant_not_found
                        )
                        else -> dataErrorRemote.toUiText()
                    }
                    _state.update { it.copy(
                        searchError = errorMessage,
                        canAddParticipant = false,
                        isSearchingParticipants = false,
                        currentSearchResult = null
                    ) }
                }
        }
    }

    private fun addParticipantToSelected() {
        state.value.currentSearchResult?.let { participantUi ->
            val isAlreadySelected = state.value.selectedChatParticipants.any {
                it.id == participantUi.id
            }
            val isAlreadyInChat = state.value.existingChatParticipants.any {
                it.id == participantUi.id
            }

            val updatedParticipants = if (isAlreadySelected || isAlreadyInChat)
                                          state.value.selectedChatParticipants
                                      else
                                          state.value.selectedChatParticipants + participantUi

            state.value.queryTextFieldState.clearText()
            _state.update { it.copy(
                selectedChatParticipants = updatedParticipants,
                currentSearchResult = null,
                searchError = null,
                isSearchingParticipants = false,
                canAddParticipant = true
            ) }
        }
    }

    private fun addParticipantsToChat() {
        if (state.value.selectedChatParticipants.isEmpty())
            return

        val chatId = _chatId.value
            ?: return

        val selectedParticipants = state.value.selectedChatParticipants
        val selectedUserIds = selectedParticipants.map { it.id }

        viewModelScope.launch {
            chatRepository
                .addParticipantsToChat(
                    chatId = chatId,
                    userIds = selectedUserIds
                )
                .onSuccess {
                    eventChannel.send(
                        element = ManageChatEvent.OnParticipantsAdded
                    )
                }
                .onFailure { exception ->
                    _state.update { it.copy(
                        isSubmitting = false,
                        submitError = exception.toUiText(),
                    ) }
                }
        }
    }
}