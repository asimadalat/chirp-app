package com.asimorphic.chat.data.network

class IosNetworkCancellationException(
    message: String,
    cause: Throwable?
): Exception(
    message = message,
    cause = cause
)