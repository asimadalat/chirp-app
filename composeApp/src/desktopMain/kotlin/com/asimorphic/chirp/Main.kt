package com.asimorphic.chirp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.asimorphic.chirp.application.ApplicationViewModel
import com.asimorphic.chirp.application.ChirpTrayMenu
import com.asimorphic.chirp.deeplink.DesktopDeepLinkHandler
import com.asimorphic.chirp.di.desktopModule
import com.asimorphic.chirp.di.initKoin
import com.asimorphic.chirp.navigation.ExternalUriHandler
import com.asimorphic.chirp.theme.rememberApplicationTheme
import com.asimorphic.chirp.window.ChirpWindow
import org.koin.compose.koinInject

fun main(args: Array<String>) {
    System.setProperty(
        "apple.awt.application.name",
        "Chirp"
    )
    initKoin {
        modules(desktopModule)
    }

    DesktopDeepLinkHandler.setup()
    val initialDeepLink = args.firstOrNull {
        val cleanedDeepLink = it.trim('"')

        DesktopDeepLinkHandler.supportedUrlPatterns.any { pattern ->
            pattern.matches(cleanedDeepLink)
        }
    }?.trim('"')

    application {
        val applicationViewModel = koinInject<ApplicationViewModel>()
        val applicationState by applicationViewModel.state.collectAsState()
        val windows = applicationState.windows

        var canReceiveDeepLink by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(canReceiveDeepLink) {
            if (canReceiveDeepLink && initialDeepLink != null)
                ExternalUriHandler.onNewUri(initialDeepLink)
        }

        LaunchedEffect(windows) {
            if (windows.isEmpty()) exitApplication()
        }

        val applicationTheme = rememberApplicationTheme(applicationState.themePreference)

        for (window in windows) {
            key(window.id) {
                ChirpWindow(
                    onAddWindowClick = applicationViewModel::onAddWindowClick,
                    onCloseRequest = { applicationViewModel.onWindowCloseRequest(window.id) },
                    onDeepLinkListenerSetup = { canReceiveDeepLink = true },
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