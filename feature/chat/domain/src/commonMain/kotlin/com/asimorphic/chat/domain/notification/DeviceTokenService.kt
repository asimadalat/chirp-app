package com.asimorphic.chat.domain.notification

import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult

interface DeviceTokenService {
    suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote>

    suspend fun deregisterToken(
        token: String
    ): EmptyResult<DataError.Remote>
}