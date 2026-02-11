@file:OptIn(FlowPreview::class)

package com.asimorphic.chat.presentation.create_chat

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.error_participant_not_found
import com.asimorphic.chat.domain.chat.ChatParticipantService
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.presentation.mapper.toUi
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import com.asimorphic.core.presentation.mapper.toUiText
import com.asimorphic.core.presentation.util.UiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class CreateChatViewModel(
    private val chatRepository: ChatRepository,
    private val chatParticipantService: ChatParticipantService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<CreateChatEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(CreateChatState())

    private val searchFlow = snapshotFlow { _state.value.queryTextFieldState.text.toString() }
        .debounce(timeout = 1.seconds)
        .onEach { query ->
            searchParticipant(query)
        }

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                searchFlow.launchIn(scope = viewModelScope)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateChatState()
        )

    fun onAction(action: CreateChatAction) {
        when (action) {
            CreateChatAction.OnAddClick -> addParticipant()
            CreateChatAction.OnCreateChatClick -> createChat()
            else -> Unit
        }
    }

    private fun createChat() {
        val userIds = state.value.selectedChatParticipants.map { it.id }
        if (userIds.isEmpty())
            return

        viewModelScope.launch {
            _state.update { it.copy(
                isCreatingChat = true,
                canAddParticipant = false,
            ) }

            chatRepository
                .createChat(otherUserIds = userIds)
                .onSuccess { chat ->
                    _state.update { it.copy(
                        isCreatingChat = false
                    ) }
                    eventChannel.send(element = CreateChatEvent.OnChatCreated(chat = chat))
                }
                .onFailure { dataErrorRemote ->
                    _state.update { it.copy(
                        isCreatingChat = false,
                        createChatError = dataErrorRemote.toUiText(),
                        canAddParticipant = it.currentSearchResult != null
                                && !it.isSearchingParticipants
                    ) }
                }
        }
    }

    private fun addParticipant() {
        state.value.currentSearchResult?.let { newParticipant ->
            val isAlreadyAddedToChat = state.value.selectedChatParticipants.any {
                it.id == newParticipant.id
            }

            if (!isAlreadyAddedToChat) {
                _state.update { it.copy(
                    selectedChatParticipants = it.selectedChatParticipants + newParticipant,
                    canAddParticipant = false,
                    currentSearchResult = null
                ) }
                _state.value.queryTextFieldState.clearText()
            }
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
}