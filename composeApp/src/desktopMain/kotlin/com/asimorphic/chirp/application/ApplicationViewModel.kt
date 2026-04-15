package com.asimorphic.chirp.application

import com.asimorphic.chirp.window.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ApplicationViewModel(
    private val applicationScope: CoroutineScope
) {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = ApplicationState()
        )

    fun onAddWindowClick() = _state.update { it.copy(
        windows = it.windows + WindowState()
    ) }

    fun onWindowCloseRequest(id: String) = _state.update { it.copy(
        windows = it.windows.filter { windowState ->
            windowState.id != id
        }
    ) }
}