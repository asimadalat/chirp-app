package com.asimorphic.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class AppLifecycleObserver {
    actual val hasForeground: Flow<Boolean>
        get() = flowOf(true) // Shouldn't auto disconnect in background on desktop
}