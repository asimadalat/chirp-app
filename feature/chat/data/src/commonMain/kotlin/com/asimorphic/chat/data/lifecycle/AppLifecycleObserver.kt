package com.asimorphic.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow

expect class AppLifecycleObserver {
    val hasForeground: Flow<Boolean>
}