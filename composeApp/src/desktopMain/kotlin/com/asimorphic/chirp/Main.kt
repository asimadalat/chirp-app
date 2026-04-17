package com.asimorphic.chirp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.window.application
import com.asimorphic.chirp.application.ApplicationViewModel
import com.asimorphic.chirp.application.ChirpTrayMenu
import com.asimorphic.chirp.di.desktopModule
import com.asimorphic.chirp.di.initKoin
import com.asimorphic.chirp.theme.rememberApplicationTheme
import com.asimorphic.chirp.window.ChirpWindow
import org.koin.compose.koinInject

fun main() {
    System.setProperty(
        "apple.awt.application.name",
        "Chirp"
    )
    initKoin {
        modules(desktopModule)
    }
    application {
        val applicationViewModel = koinInject<ApplicationViewModel>()
        val applicationState by applicationViewModel.state.collectAsState()
        val windows = applicationState.windows

        LaunchedEffect(windows) {
            if (windows.isEmpty()) exitApplication()
        }

        val applicationTheme = rememberApplicationTheme(applicationState.themePreference)

        for (window in windows) {
            key(window.id) {
                ChirpWindow(
                    onAddWindowClick = applicationViewModel::onAddWindowClick,
                    onCloseRequest = { applicationViewModel.onWindowCloseRequest(window.id) },
                    onFocusChanged = { isFocused ->
                        applicationViewModel.onWindowFocusChanged(window.id, isFocused)
                    },
                    applicationTheme = applicationTheme
                )
            }
        }

        ChirpTrayMenu(
            state = applicationState.trayState,
            setThemePreference = applicationState.themePreference,
            onThemePreferenceClick = applicationViewModel::onThemePreferenceClick
        )
    }
}