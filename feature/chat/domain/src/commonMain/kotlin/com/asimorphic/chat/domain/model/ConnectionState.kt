package com.asimorphic.chat.domain.model

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR_NETWORK,
    ERROR_UNKNOWN
}