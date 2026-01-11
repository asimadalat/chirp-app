package com.asimorphic.chirp

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.asimorphic.auth.presentation.navigation.AuthGraphRoutes
import com.asimorphic.chat.presentation.chat_menu.ChatMenuRoute
import com.asimorphic.chirp.main.MainEvent
import com.asimorphic.chirp.main.MainViewModel
import com.asimorphic.chirp.navigation.DeepLinkListener
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.asimorphic.chirp.navigation.NavigationRoot
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    onAuthStatusChecked: () -> Unit,
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    DeepLinkListener(navController = navController)

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.isCheckingAuthStatus) {
        if (!state.isCheckingAuthStatus)
            onAuthStatusChecked()
    }

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is MainEvent.OnSessionExpired -> {
                navController.navigate(route = AuthGraphRoutes.Graph) {
                    popUpTo(route = AuthGraphRoutes.Graph) {
                        inclusive = false
                    }
                }
            }
        }
    }

    ChirpTheme {
        if (!state.isCheckingAuthStatus) {
            NavigationRoot(
                navController = navController,
                startDestination = if (state.isLoggedIn) {
                    ChatMenuRoute
                } else {
                    AuthGraphRoutes.Graph
                }
            )
        }
    }
}