@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.asimorphic.chat.data.network

import com.asimorphic.chat.data.dto.websocket.WebSocketMessageDto
import com.asimorphic.chat.data.lifecycle.AppLifecycleObserver
import com.asimorphic.chat.domain.exception.ConnectionError
import com.asimorphic.chat.domain.model.ConnectionState
import com.asimorphic.core.data.network.UrlConstants
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.log.ChirpLogger
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result
import com.asimorphic.feature.chat.data.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

class KtorWebSocketConnector(
    private val httpClient: HttpClient,
    private val json: Json,
    private val applicationScope: CoroutineScope,
    private val sessionService: SessionService,
    private val connectivityErrorHandler: ConnectivityErrorHandler,
    private val connectivityRetryHandler: ConnectivityRetryHandler,
    private val connectivityObserver: ConnectivityObserver,
    private val appLifecycleObserver: AppLifecycleObserver,
    private val logger: ChirpLogger
) {

    private val _connectionState = MutableStateFlow(value = ConnectionState.DISCONNECTED)
    val connectionState = _connectionState.asStateFlow()

    private var currentSession: WebSocketSession? = null

    private val isConnected = connectivityObserver
        .isConnected
        .debounce(
            timeout = 1.seconds
        ).stateIn(
            initialValue = false,
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5000L
            )
        )

    private val hasForeground = appLifecycleObserver
        .hasForeground
        .onEach { hasForeground ->
            if (hasForeground)
                connectivityRetryHandler.resetDelay()
        }
        .stateIn(
            initialValue = false,
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5000L
            )
        )

    val messages = combine(
        flow = sessionService.observeAuthCredential(),
        flow2 = isConnected,
        flow3 = hasForeground
    ) { authCredentials, isConnected, hasForeground ->
        when {
            authCredentials == null -> {
                logger.info(
                    message = "Authentication details missing. Disconnecting from active websocket connection"
                )
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
                connectivityRetryHandler.resetDelay()
                null
            }
            !isConnected -> {
                logger.info(
                    message = "Device disconnected. Disconnecting from active websocket connection"
                )
                _connectionState.value = ConnectionState.ERROR_NETWORK
                currentSession?.close()
                currentSession = null
                null
            }
            !hasForeground -> {
                logger.info(
                    message = "App in background. Disconnecting from active websocket connection"
                )
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
                null
            }
            else -> {
                logger.info(
                    message = "✓ Authenticated. \n✓ Internet connection established. \n✓ App in " +
                            "foreground. \nConnecting..."
                )

                if (_connectionState.value !in listOf(ConnectionState.CONNECTED, ConnectionState.CONNECTING)) {
                    _connectionState.value = ConnectionState.CONNECTING
                }

                authCredentials
            }
        }
    }.flatMapLatest { authCredential ->
        if (authCredential == null)
            emptyFlow()
        else
            createWebSocketFlow(
                accessToken = authCredential.accessToken
            )
                .catch { exception ->
                    logger.error(message = "Websocket exception", throwable = exception)
                    currentSession?.close()
                    currentSession = null

                    val transformedException = connectivityErrorHandler.transformException(
                        exception = exception
                    )
                    throw transformedException
                }
                .retryWhen { throwable, attempt ->
                    logger.info(message = "Connection failed on attempt $attempt")

                    val shouldRetry = connectivityRetryHandler
                        .shouldRetry(
                            cause = throwable,
                            attempt = attempt
                        )

                    if (shouldRetry) {
                        _connectionState.value = ConnectionState.CONNECTING
                        connectivityRetryHandler.applyRetryDelay(attempt = attempt)
                    }
                    shouldRetry
                }
                .catch { exception ->
                    logger.error(
                        message = "Unhandled websocket exception",
                        throwable = exception
                    )

                    _connectionState.value = connectivityErrorHandler.getConnectionStateForError(
                        cause = exception
                    )
                }
    }

    private fun createWebSocketFlow(accessToken: String) = callbackFlow {
        _connectionState.value = ConnectionState.CONNECTING

        currentSession = httpClient.webSocketSession(
            urlString = "${UrlConstants.BASE_WS_URL}/chat"
        ) {
           header(
               key = "x-api-key",
               value = BuildKonfig.API_KEY
           )
           header(
               key = "",
               value = "Bearer $accessToken"
           )
        }

        currentSession?.let { session ->
            _connectionState.value = ConnectionState.CONNECTED

            session
                .incoming
                .consumeAsFlow()
                .buffer(
                    capacity = 100
                )
                .collect { frame ->
                    when (frame) {
                        is Frame.Ping -> {
                            logger.debug(
                                message = "Received ping from server, sending pong..."
                            )
                            session.send(
                                frame = Frame.Pong(
                                    data = frame.data
                                )
                            )
                        }
                        is Frame.Text -> {
                            val text = frame.readText()
                            logger.info(
                                message = "Received rawtext frame: $text"
                            )

                            val messageDto = json.decodeFromString<WebSocketMessageDto>(string = text)
                            send(element = messageDto)
                        }
                        else -> Unit
                    }
                }
        } ?: throw Exception(message = "Failed to establish websocket connection")

        awaitClose {
            launch(context = NonCancellable) {
                logger.info(
                    message = "Disconnected from active websocket session..."
                )
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
            }
        }
    }

    suspend fun sendMessage(message: String): EmptyResult<ConnectionError> {
        val connectionState = connectionState.value

        if (currentSession == null || connectionState != ConnectionState.CONNECTED)
            return Result.Failure(error = ConnectionError.NOT_CONNECTED)

        return try {
            currentSession?.send(content = message)
            Result.Success(data = Unit)
        } catch (exception: Exception) {
            currentCoroutineContext().ensureActive()
            logger.error(message = "Could not send websocket message", throwable = exception)
            Result.Failure(error = ConnectionError.MESSAGE_SEND_FAILED)
        }
    }
}