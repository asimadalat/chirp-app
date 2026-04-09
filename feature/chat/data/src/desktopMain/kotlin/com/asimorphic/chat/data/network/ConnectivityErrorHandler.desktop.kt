package com.asimorphic.chat.data.network

import com.asimorphic.chat.domain.model.ConnectionState

actual class ConnectivityErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        return ConnectionState.ERROR_NETWORK
    }

    actual fun transformException(exception: Throwable): Throwable {
        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        return true
    }
}