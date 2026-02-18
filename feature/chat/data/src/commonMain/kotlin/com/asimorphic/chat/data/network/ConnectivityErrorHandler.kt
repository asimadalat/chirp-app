package com.asimorphic.chat.data.network

import com.asimorphic.chat.domain.model.ConnectionState

expect class ConnectivityErrorHandler {
    fun getConnectionStateForError(cause: Throwable): ConnectionState

    fun transformException(exception: Throwable): Throwable

    fun isRetriableError(cause: Throwable): Boolean
}