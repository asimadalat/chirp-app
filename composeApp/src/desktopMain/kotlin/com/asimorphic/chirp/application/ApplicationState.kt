package com.asimorphic.chirp.application

import androidx.compose.ui.window.TrayState
import com.asimorphic.chirp.window.WindowState
import com.asimorphic.core.domain.preferences.ThemePreference

data class ApplicationState(
    val trayState: TrayState = TrayState(),
    val windows: List<WindowState> = listOf(WindowState()),
    val themePreference: ThemePreference = ThemePreference.SYSTEM
)
