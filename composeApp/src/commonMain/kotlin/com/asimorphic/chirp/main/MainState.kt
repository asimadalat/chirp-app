package com.asimorphic.chirp.main

data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuthStatus: Boolean = true
)
