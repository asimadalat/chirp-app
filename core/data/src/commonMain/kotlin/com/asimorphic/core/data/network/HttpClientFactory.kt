package com.asimorphic.core.data.network

import com.asimorphic.core.data.BuildKonfig
import com.asimorphic.core.data.dto.AuthCredentialDto
import com.asimorphic.core.data.dto.request.RefreshRequest
import com.asimorphic.core.data.mapper.toModel
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.log.ChirpLogger
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val chirpLogger: ChirpLogger,
    private val sessionService: SessionService
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
            install(plugin = Auth) {
                bearer {
                    loadTokens {
                        sessionService.observeAuthCredential()
                            .firstOrNull()
                            ?.let {
                                BearerTokens(
                                    accessToken = it.accessToken,
                                    refreshToken = it.refreshToken
                                )
                            }
                    }
                    refreshTokens {
                        if (response.request.url.encodedPath.contains(other = "auth/"))
                            return@refreshTokens null

                        val authCredential = sessionService.observeAuthCredential().firstOrNull()
                        if (authCredential?.refreshToken.isNullOrBlank()) {
                            sessionService.set(null)
                            return@refreshTokens null
                        }

                        var bearerTokens: BearerTokens? = null
                        client.post<RefreshRequest, AuthCredentialDto>(
                            route = "/auth/refresh",
                            body = RefreshRequest(
                                refreshToken = authCredential.refreshToken
                            ),
                            builder = {
                                markAsRefreshTokenRequest()
                            }
                        ).onSuccess { newAuthCredential ->
                            sessionService.set(newAuthCredential.toModel())
                            bearerTokens = BearerTokens(
                                accessToken = newAuthCredential.accessToken,
                                refreshToken = newAuthCredential.refreshToken
                            )
                        }.onFailure { error ->
                            sessionService.set(null)
                        }

                        bearerTokens
                    }
                }
            }
        }
    }
}