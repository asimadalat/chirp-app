package com.asimorphic.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.asimorphic.auth.presentation.navigation.AuthGraphRoutes
import com.asimorphic.auth.presentation.navigation.authGraph
import com.asimorphic.chat.presentation.navigation.ChatGraphRoutes
import com.asimorphic.chat.presentation.navigation.chatGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(route = ChatGraphRoutes.Graph) {
                    popUpTo(route = AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
        chatGraph(
            navController = navController
        )
    }
}