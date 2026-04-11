package com.asimorphic.core.data.network

import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.UnknownHostException

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch (ex: UnknownHostException) {
        Result.Failure(error = DataError.Remote.NO_INTERNET)
    } catch (ex: UnresolvedAddressException) {
        Result.Failure(error = DataError.Remote.NO_INTERNET)
    } catch (ex: ConnectException) {
        Result.Failure(error = DataError.Remote.NO_INTERNET)
    } catch (ex: HttpRequestTimeoutException) {
        Result.Failure(error = DataError.Remote.REQUEST_TIMEOUT)
    } catch (ex: SocketTimeoutException) {
        Result.Failure(error = DataError.Remote.REQUEST_TIMEOUT)
    } catch (ex: SerializationException) {
        Result.Failure(error = DataError.Remote.SERIALIZATION)
    } catch (ex: Exception) {
        currentCoroutineContext().ensureActive()
        Result.Failure(error = DataError.Remote.UNKNOWN)
    }
}