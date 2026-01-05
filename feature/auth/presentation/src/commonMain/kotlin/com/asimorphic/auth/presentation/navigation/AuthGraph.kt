package com.asimorphic.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.asimorphic.auth.presentation.email_verification.EmailVerificationRoot
import com.asimorphic.auth.presentation.register.RegisterRoot
import com.asimorphic.auth.presentation.register_success.RegisterSuccessRoot

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Register
    ) {
        composable<AuthGraphRoutes.Register> {
            RegisterRoot(
                onRegisterSuccess = {
                    navController.navigate(
                        route = AuthGraphRoutes.RegisterSuccess(
                            email = it
                        )
                    )
                }
            )
        }
        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessRoot()
        }
        composable<AuthGraphRoutes.EmailVerification>(
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = "https://api.chirp.asimorphic.dev/api/auth/verify-email?token={token}"
                },
                navDeepLink {
                    this.uriPattern = "chirp://api.chirp.asimorphic.dev/api/auth/verify-email?token={token}"
                },
            )
        ) {
            EmailVerificationRoot()
        }
    }
}