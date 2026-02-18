package com.asimorphic.chat.data.network

import com.asimorphic.chat.domain.model.ConnectionState
import kotlinx.coroutines.CancellationException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorTimedOut

actual class  ConnectivityErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        val nsError = extractNSError(cause)

        return if (nsError != null) {
            when (nsError.code) {
                NSURLErrorTimedOut,
                NSURLErrorNotConnectedToInternet,
                NSURLErrorNetworkConnectionLost -> ConnectionState.ERROR_NETWORK
                else -> ConnectionState.ERROR_UNKNOWN
            }
        } else if (cause is IosNetworkCancellationException) {
            ConnectionState.ERROR_NETWORK
        } else ConnectionState.ERROR_UNKNOWN
    }

    actual fun transformException(exception: Throwable): Throwable {
        if (exception is CancellationException) {
            val cause = exception.cause
                ?: return exception

            val isDarwinException = cause.message?.contains(
                other = "DarwinHttpRequestException"
            ) == true
            val isNotConnectedException = cause.message?.contains(
                other = "NSURLErrorDomain Code=-1009"
            ) == true
            val isConnectionLostException = cause.message?.contains(
                other = "NSURLErrorDomain Code=-1005"
            ) == true

            if (isDarwinException || isNotConnectedException || isConnectionLostException) {
                return IosNetworkCancellationException(
                    message = "Network connection lost",
                    cause = cause
                )
            }
        }

        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        if (cause is IosNetworkCancellationException)
            return true

        return when (extractNSError(cause)?.code) {
            NSURLErrorTimedOut,
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost -> true
            else -> false
        }
    }

    private fun extractNSError(cause: Throwable): NSError? {
        val throwableCause = cause.cause
        if (throwableCause is NSError)
            return throwableCause

        if (cause is NSError)
            return cause

        val exceptionNSError = cause.toNSError()
        val causeNSError = cause.cause?.toNSError()

        return exceptionNSError
            ?: causeNSError
    }

    private fun Throwable.toNSError(): NSError? {
        return message?.let { message ->
            when {
                message.contains(other = NSURLErrorNotConnectedToInternetPattern) -> {
                    return NSError.errorWithDomain(
                        domain = NSURLErrorDomain,
                        code = NSURLErrorNotConnectedToInternet,
                        userInfo = null
                    )
                }
                message.contains(other = NSURLErrorNetworkConnectionLostPattern) -> {
                    return NSError.errorWithDomain(
                        domain = NSURLErrorDomain,
                        code = NSURLErrorNetworkConnectionLost,
                        userInfo = null
                    )
                }
                else -> null
            }
        }
    }

    companion object {
        private val NSURLErrorNotConnectedToInternetPattern =
            "Error Domain=${NSURLErrorDomain} Code=${NSURLErrorNotConnectedToInternet}"

        private val NSURLErrorNetworkConnectionLostPattern =
            "Error Domain=${NSURLErrorDomain} Code=${NSURLErrorNetworkConnectionLost}"
    }
}