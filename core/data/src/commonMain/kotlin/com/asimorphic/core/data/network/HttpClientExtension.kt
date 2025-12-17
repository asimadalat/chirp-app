package com.asimorphic.core.data.network

import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

fun constructRoute(route: String): String {
    return when {
        route.contains(other = UrlConstants.BASE_HTTP_URL) -> route
        route.startsWith(prefix = "/") -> "${UrlConstants.BASE_HTTP_URL}$route"
        else -> "${UrlConstants.BASE_HTTP_URL}$route"
    }
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(data = response.body<T>())
            } catch (ex: NoTransformationFoundException) {
                Result.Failure(error = DataError.Remote.SERIALIZATION)
            }
        }
        400 -> Result.Failure(error = DataError.Remote.BAD_REQUEST)
        401 -> Result.Failure(error = DataError.Remote.UNAUTHORIZED)
        403 -> Result.Failure(error = DataError.Remote.FORBIDDEN)
        404 -> Result.Failure(error = DataError.Remote.NOT_FOUND)
        408 -> Result.Failure(error = DataError.Remote.REQUEST_TIMEOUT)
        409 -> Result.Failure(error = DataError.Remote.CONFLICT)
        413 -> Result.Failure(error = DataError.Remote.PAYLOAD_TOO_LARGE)
        429 -> Result.Failure(error = DataError.Remote.TOO_MANY_REQUESTS)
        500 -> Result.Failure(error = DataError.Remote.INTERNAL_SERVER_ERROR)
        503 -> Result.Failure(error = DataError.Remote.SERVICE_UNAVAILABLE)
        else -> Result.Failure(error = DataError.Remote.UNKNOWN)
    }
}

expect suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend(HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote>

suspend inline fun <reified T> safeCall(
    noinline execute: suspend () -> HttpResponse
): Result<T, DataError.Remote> {
    return platformSafeCall(
        execute = execute
    ) { response ->
        responseToResult(response)
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.post(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {

    return safeCall {
        post {
            url(urlString = constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key = key, value = value)
            }
            setBody(body)
            builder()
        }
    }
}

suspend inline fun <reified Response: Any> HttpClient.get(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {

    return safeCall {
        get {
            url(urlString = constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key = key, value = value)
            }
            builder()
        }
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.put(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {

    return safeCall {
        put {
            url(urlString = constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key = key, value = value)
            }
            setBody(body)
            builder()
        }
    }
}

suspend inline fun <reified Response: Any> HttpClient.delete(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Remote> {

    return safeCall {
        delete {
            url(urlString = constructRoute(route))
            queryParams.forEach { (key, value) ->
                parameter(key = key, value = value)
            }
            builder()
        }
    }
}