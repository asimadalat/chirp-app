package com.asimorphic.core.data.network

import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import platform.Foundation.NSURLErrorCallIsActive
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDNSLookupFailed
import platform.Foundation.NSURLErrorDataNotAllowed
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorInternationalRoamingOff
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorResourceUnavailable
import platform.Foundation.NSURLErrorTimedOut

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch (ex: DarwinHttpRequestException) {
        handleDarwinException(ex)
    } catch (ex: UnresolvedAddressException) {
        Result.Failure(error = DataError.Remote.NO_INTERNET)
    } catch (ex: HttpRequestTimeoutException) {
        Result.Failure(error = DataError.Remote.REQUEST_TIMEOUT)
    } catch (ex: SerializationException) {
        Result.Failure(error = DataError.Remote.SERIALIZATION)
    } catch (ex: Exception) {
        currentCoroutineContext().ensureActive()
        Result.Failure(error = DataError.Remote.UNKNOWN)
    }
}

private fun handleDarwinException(
    ex: DarwinHttpRequestException
): Result<Nothing, DataError.Remote> {
    val nsError = ex.origin

    return if (nsError.domain == NSURLErrorDomain) {
        when (nsError.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorCannotFindHost,
            NSURLErrorDNSLookupFailed,
            NSURLErrorResourceUnavailable,
            NSURLErrorInternationalRoamingOff,
            NSURLErrorCallIsActive,
            NSURLErrorDataNotAllowed -> Result.Failure(error = DataError.Remote.NO_INTERNET)

            NSURLErrorTimedOut -> Result.Failure(error = DataError.Remote.REQUEST_TIMEOUT)

            else -> Result.Failure(error = DataError.Remote.UNKNOWN)
        }
    } else Result.Failure(error = DataError.Remote.UNKNOWN)
}