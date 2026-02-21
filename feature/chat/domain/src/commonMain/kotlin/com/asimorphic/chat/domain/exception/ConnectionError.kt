package com.asimorphic.chat.domain.exception

import com.asimorphic.core.domain.util.Error

enum class ConnectionError: Error {
    NOT_CONNECTED,
    MESSAGE_SEND_FAILED
}