package com.asimorphic.chat.presentation.chat_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.core.domain.auth.SessionService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatMenuViewModel(
    private val sessionService: SessionService
): ViewModel() {

    init {
        viewModelScope.launch {
            delay(timeMillis = 5000)
            sessionService.set(null)
        }
    }
}