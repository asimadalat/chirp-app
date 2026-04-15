package com.asimorphic.chirp.application

import com.asimorphic.chirp.window.WindowState

data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState())
)
