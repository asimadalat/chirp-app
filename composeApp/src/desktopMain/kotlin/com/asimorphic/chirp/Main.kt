package com.asimorphic.chirp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.asimorphic.chirp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            title = "Chirp",
            onCloseRequest = ::exitApplication
        ) {
            App()
        }
    }
}