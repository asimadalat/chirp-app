package com.asimorphic.chat.data.network

import kotlinx.coroutines.delay
import kotlin.math.pow

class ConnectivityRetryHandler(
    private val connectivityErrorHandler: ConnectivityErrorHandler
) {
    private var shouldSkipBackoff = false

    fun shouldRetry(
        cause: Throwable,
        attempt: Long
    ): Boolean {
        return connectivityErrorHandler.isRetriableError(
            cause = cause
        )
    }

    suspend fun applyRetryDelay(attempt: Long) {
        if (!shouldSkipBackoff) {
            val delay = createBackoffDelay(attempt = attempt)
            delay(timeMillis = delay)
        } else {
            shouldSkipBackoff = false
        }
    }

    fun resetDelay(): Unit {
        shouldSkipBackoff = true
    }

    private fun createBackoffDelay(attempt: Long): Long {
        val delayInterval = (2f.pow(n = attempt.toInt()) * 2000L).toLong()
        val maxDelayInterval = 30_000L

        return minOf(
            a = delayInterval,
            b = maxDelayInterval
        )
    }
}