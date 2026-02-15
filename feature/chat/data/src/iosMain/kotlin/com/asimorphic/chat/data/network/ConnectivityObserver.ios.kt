package com.asimorphic.chat.data.network

import kotlinx.coroutines.flow.Flow

actual class ConnectivityObserver {
    actual val isConnected: Flow<Boolean>
        get() = TODO("Not yet implemented")
}