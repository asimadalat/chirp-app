package com.asimorphic.chat.data.network

import com.asimorphic.chat.domain.model.ConnectionState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.network.sockets.SocketTimeoutException
import kotlinx.io.EOFException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

actual class ConnectivityErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        return when (cause) {
            is WebSocketException,
            is SocketException,
            is SocketTimeoutException,
            is ClientRequestException,
            is UnknownHostException,
            is SSLException,
            is EOFException -> ConnectionState.ERROR_NETWORK
            else -> ConnectionState.ERROR_UNKNOWN
        }
    }

    actual fun transformException(exception: Throwable): Throwable {
        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        return when (cause) {
            is WebSocketException,
            is SocketException,
            is SocketTimeoutException,
            is EOFException -> true
            else -> false
        }
    }
}