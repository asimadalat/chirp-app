package com.asimorphic.chirp.main

sealed interface MainEvent {
    data object OnSessionExpired: MainEvent
}