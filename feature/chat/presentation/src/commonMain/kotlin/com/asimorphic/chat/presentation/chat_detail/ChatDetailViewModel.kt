@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package com.asimorphic.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.chat.domain.chat.ChatConnectionService
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.domain.chat_message.ChatMessageRepository
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.domain.model.ConnectionState
import com.asimorphic.chat.domain.model.OutgoingNewMessage
import com.asimorphic.chat.presentation.mapper.toUi
import com.asimorphic.chat.presentation.mapper.toUiList
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.util.DataErrorException
import com.asimorphic.core.domain.util.Paginator
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import com.asimorphic.core.presentation.mapper.toUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatDetailViewModel(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatConnectionService: ChatConnectionService,
    private val sessionService: SessionService
) : ViewModel() {

    private val eventChannel = Channel<ChatDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _chatId = MutableStateFlow<String?>(value = null)

    private var hasLoadedInitialData = false

    private var currentPaginator: Paginator<String?, ChatMessage>? = null

    private val chatInfoFlow = _chatId
        .onEach { chatId ->
            if (chatId == null)
                currentPaginator = null
            else
                configurePaginatorForChat(chatId = chatId)
        }
        .flatMapLatest { chatId ->
            if (chatId != null)
                chatRepository.getChatInfoById(chatId = chatId)
            else
                emptyFlow()
        }

    private val _state = MutableStateFlow(ChatDetailState())

    private val canSendMessage = snapshotFlow {
        _state.value.messageTextFieldState.text.toString()
    }.map {
        it.isBlank()
    }.combine(flow = chatConnectionService.connectionState) { isMessageBlank, connectionState ->
        !isMessageBlank && connectionState == ConnectionState.CONNECTED
    }

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
            ),
            messages = chatInfo.messages.toUiList(
                selfUserId = authCredential.user.id
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
                observeConnectionState()
                observeChatMessages()
                observeCanSendMessage()

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
            ChatDetailAction.OnChatOptionsClick -> onChatOptionsClick()
            ChatDetailAction.OnDismissChatOptions -> onDismissChatOptions()
            ChatDetailAction.OnLeaveChatClick -> onLeaveChatClick()
            ChatDetailAction.OnSendMessageClick -> sendMessage()
            is ChatDetailAction.OnRetryClick -> retryMessage(messageUi = action.message)
            is ChatDetailAction.OnDeleteMessageClick -> deleteMessage(messageUi = action.message)
            ChatDetailAction.OnDismissMessageOptions -> onDismissMessageOptions()
            is ChatDetailAction.OnMessageLongClick -> onMessageLongClick(messageUi = action.message)
            ChatDetailAction.OnBackClick -> {}
            ChatDetailAction.OnPeopleClick -> {}
            ChatDetailAction.OnScrollToTop -> onScrollToTop()
            ChatDetailAction.OnPaginationRetryClick -> retryPagination()
        }
    }

    private fun loadNextItems() {
        viewModelScope.launch {
            currentPaginator?.loadNextItems()
        }
    }

    private fun retryPagination() = loadNextItems()

    private fun onScrollToTop() = loadNextItems()

    private fun onMessageLongClick(messageUi: MessageUi.SelfParticipantMessage) {
        _state.update { it.copy(
            messageWithOptionsOpen = messageUi
        ) }
    }

    private fun onDismissMessageOptions() {
        _state.update { it.copy(
            messageWithOptionsOpen = null
        ) }
    }

    private fun deleteMessage(messageUi: MessageUi.SelfParticipantMessage) {
        viewModelScope.launch {
            chatMessageRepository
                .deleteMessage(
                    messageId = messageUi.id
                )
                .onFailure { exception ->
                    eventChannel.send(
                        element = ChatDetailEvent.OnError(error = exception.toUiText())
                    )
                }
        }
    }

    private fun retryMessage(messageUi: MessageUi.SelfParticipantMessage) {
        viewModelScope.launch {
            chatMessageRepository
                .retryMessage(
                    messageId = messageUi.id
                )
                .onFailure { exception ->
                    eventChannel.send(
                        element = ChatDetailEvent.OnError(error = exception.toUiText())
                    )
                }
        }
    }

    private fun sendMessage() {
        val chatId = _chatId.value
        val content = state.value.messageTextFieldState.text.toString().trim()
        if (chatId == null || content.isBlank())
            return

        viewModelScope.launch {
            val message = OutgoingNewMessage(
                chatId = chatId,
                messageId = Uuid.random().toString(),
                content = content
            )

            chatMessageRepository
                .sendMessage(
                    message = message
                )
                .onSuccess {
                    state.value.messageTextFieldState.clearText()
                }
                .onFailure { exception ->
                    eventChannel.send(
                        element = ChatDetailEvent.OnError(
                            error = exception.toUiText()
                        )
                    )
                }
        }
    }

    private fun observeConnectionState() {
        chatConnectionService
            .connectionState
            .onEach { connectionState ->
                when (connectionState) {
                    ConnectionState.CONNECTED -> {
                        currentPaginator?.loadNextItems()
                    }
                    else -> Unit
                }

                _state.update { it.copy(
                    connectionState = connectionState
                ) }
            }
            .launchIn(
                scope = viewModelScope
            )
    }

    private fun observeChatMessages() {
        val existingMessages = state
            .map {
                it.messages
            }
            .distinctUntilChanged()

        val newMessages = _chatId
            .flatMapLatest { chatId ->
                if (chatId != null)
                    chatMessageRepository.getMessagesForChat(chatId = chatId)
                else
                    emptyFlow()
            }

        val isNearBottom = state
            .map {
                it.isNearBottom
            }
            .distinctUntilChanged()

        combine(
            flow = existingMessages,
            flow2 = newMessages,
            flow3 = isNearBottom
        ) { existingMessages, newMessages, isNearBottom ->
            val lastExistingMessageId = existingMessages.lastOrNull()?.id
            val lastNewMessageId = newMessages.lastOrNull()?.message?.id

            if (lastExistingMessageId != lastNewMessageId && isNearBottom)
                eventChannel.send(element = ChatDetailEvent.OnNewMessage)
        }.launchIn(
            scope = viewModelScope
        )
    }

    private fun observeCanSendMessage() {
        canSendMessage
            .onEach { canSendMessage ->
                _state.update { it.copy(
                    canSendMessage = canSendMessage
                ) }
            }
            .launchIn(
                scope = viewModelScope
            )
    }

    private fun onLeaveChatClick() {
        val chatId = _chatId.value
            ?: return

        _state.update { it.copy(
            isChatOptionsOpen = false
        ) }

        viewModelScope.launch {
            chatRepository
                .leaveChat(
                    chatId = chatId
                )
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()

                    _chatId.update { null }
                    _state.update { it.copy(
                        chatUi = null,
                        messages = emptyList(),
                        chipState = ChipState()
                    ) }
                }
                .onFailure { exception ->
                    eventChannel.send(
                        element = ChatDetailEvent.OnError(
                            error = exception.toUiText()
                        )
                    )
                }
        }
    }

    private fun onDismissChatOptions() {
        _state.update { it.copy(
            isChatOptionsOpen = false
        ) }
    }

    private fun onChatOptionsClick() {
        _state.update { it.copy(
            isChatOptionsOpen = true
        ) }
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

    private fun configurePaginatorForChat(chatId: String) {
        currentPaginator = Paginator(
            initialKey = null,
            onLoadUpdated = { isLoading ->
                _state.update { it.copy(
                    isPaginationLoading = isLoading
                ) }
            },
            onRequest = { before ->
                chatMessageRepository.fetchMessages(
                    chatId = chatId,
                    before = before
                )
            },
            getNextKey = { messages ->
                messages.minOfOrNull {
                    it.createdAt
                }.toString()
            },
            onSuccess = { messages, _ ->
                _state.update { it.copy(
                    paginationEndReached = messages.isEmpty(),
                    paginationError = null
                ) }
            },
            onError = { exception ->
                if (exception is DataErrorException) {
                    _state.update { it.copy(
                        paginationError = exception.error.toUiText()
                    ) }
                }
            }
        )

        _state.update { it.copy(
            paginationEndReached = false,
            isPaginationLoading = false
        ) }
    }
}