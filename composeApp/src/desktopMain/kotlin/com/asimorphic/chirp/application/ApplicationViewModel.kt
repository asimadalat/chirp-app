package com.asimorphic.chirp.application

import androidx.compose.ui.window.Notification
import com.asimorphic.chat.data.notification.DesktopNotifier
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
    private val themePreferences: ThemePreferences,
    private val desktopNotifier: DesktopNotifier
) {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeNewMessages()
                observeThemePreference()

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.Lazily,
            initialValue = ApplicationState()
        )
    
    fun observeNewMessages() {
        desktopNotifier
            .observeNewNotifications()
            .onEach { notificationPayload ->
                val isAppForeground = state.value.windows.any { it.isFocused }

                if (isAppForeground) {
                    state.value.trayState.sendNotification(
                        notification = Notification(
                            title = notificationPayload.title,
                            message = notificationPayload.message,
                            type = Notification.Type.Info
                        )
                    )
                }
            }
            .launchIn(
                scope = applicationScope
            )
    }

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

    fun onWindowFocusChanged(id: String, isFocused: Boolean) {
        _state.update { it.copy(
            windows = it.windows
                .map { windowState ->
                    if (windowState.id == id) {
                        windowState.copy(isFocused = isFocused)
                    } else windowState
                }
        ) }
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