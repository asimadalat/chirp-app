package com.asimorphic.chirp.application

import com.asimorphic.chirp.window.WindowState
import com.asimorphic.core.domain.preferences.ThemePreference
import com.asimorphic.core.domain.preferences.ThemePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApplicationViewModel(
    private val applicationScope: CoroutineScope,
    private val themePreferences: ThemePreferences
) {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeThemePreference()

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = ApplicationState()
        )

    fun observeThemePreference() {
        themePreferences
            .observeThemePreference()
            .onEach { themePreference ->
                _state.update { it.copy(
                    themePreference = themePreference
                ) }
            }
            .launchIn(
                scope = applicationScope
            )
    }

    fun onAddWindowClick() = _state.update { it.copy(
        windows = it.windows + WindowState()
    ) }

    fun onWindowCloseRequest(id: String) = _state.update { it.copy(
        windows = it.windows.filter { windowState ->
            windowState.id != id
        }
    ) }

    fun onThemePreferenceClick(themePreference: ThemePreference) = applicationScope.launch {
        themePreferences.updateThemePreference(
            theme = themePreference
        )
    }
}