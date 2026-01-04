package com.asimorphic.chirp

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.asimorphic.chirp.navigation.NavigationRoot
import com.asimorphic.core.designsystem.theme.ChirpTheme

@Composable
@Preview
fun App() {
    ChirpTheme {
        NavigationRoot()
    }
}