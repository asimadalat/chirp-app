package com.asimorphic.chirp

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.asimorphic.chirp.navigation.DeepLinkListener
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.asimorphic.chirp.navigation.NavigationRoot
import com.asimorphic.core.designsystem.theme.ChirpTheme

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    DeepLinkListener(navController = navController)
    ChirpTheme {
        NavigationRoot(navController = navController)
    }
}