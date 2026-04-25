package com.asimorphic.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavUri

@Composable
fun DeepLinkListener(
    navController: NavController,
    onSetup: () -> Unit
) {
    DisposableEffect(key1 = Unit) {
        ExternalUriHandler.listener = { uri ->
            navController.navigate(
                deepLink = NavUri(uriString = uri)
            )
        }

        onSetup()

        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}