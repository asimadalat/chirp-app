package com.asimorphic.chirp.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.core.domain.auth.SessionService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionService: SessionService
): ViewModel() {
    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(value = MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSession()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    private var previousRefreshToken: String? = null

    init {
        viewModelScope.launch {
            val authCredential = sessionService.observeAuthCredential().firstOrNull()
            _state.update { it.copy(
                isLoggedIn = authCredential != null,
                isCheckingAuthStatus = false
            ) }
        }
    }

    private fun observeSession() {
        sessionService.observeAuthCredential()
            .onEach { authCredential ->
                val currentRefreshToken = authCredential?.refreshToken
                val isSessionExpired = previousRefreshToken != null
                        && currentRefreshToken == null

                if (isSessionExpired) {
                    sessionService.set(null)
                    _state.update { it.copy(
                        isLoggedIn = false
                    ) }
                }
                previousRefreshToken = authCredential?.refreshToken
            }.launchIn(viewModelScope)
    }
}