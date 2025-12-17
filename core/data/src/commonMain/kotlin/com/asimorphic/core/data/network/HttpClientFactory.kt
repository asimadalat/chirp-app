package com.asimorphic.core.data.network

import com.asimorphic.core.data.BuildKonfig
import com.asimorphic.core.domain.log.ChirpLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val chirpLogger: ChirpLogger
) {

    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine = engine) {
            install(plugin = ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(plugin = HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(plugin = Logging) {
                logger = object  : Logger {
                    override fun log(message: String) {
                        chirpLogger.debug(message = message)
                    }
                }
                level = LogLevel.ALL
            }
            install(plugin = WebSockets) {
                pingIntervalMillis = 20_000L
            }
            defaultRequest {
                header(key = "x-api-key", value = BuildKonfig.API_KEY)
                contentType(type = ContentType.Application.Json)
            }
        }
    }
}