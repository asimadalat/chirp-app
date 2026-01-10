package com.asimorphic.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asimorphic.auth.presentation.navigation.AuthGraphRoutes
import com.asimorphic.auth.presentation.navigation.authGraph
import com.asimorphic.chat.presentation.chat_menu.ChatMenuRoute
import com.asimorphic.chat.presentation.chat_menu.ChatMenuRoot

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthGraphRoutes.Graph
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(route = ChatMenuRoute) {
                    popUpTo(route = AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
        composable<ChatMenuRoute> {
            ChatMenuRoot()
        }
    }
}