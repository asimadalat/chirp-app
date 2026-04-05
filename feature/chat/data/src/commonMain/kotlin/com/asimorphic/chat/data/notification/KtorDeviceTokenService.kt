package com.asimorphic.chat.data.notification

import com.asimorphic.chat.data.dto.request.RegisterDeviceTokenRequest
import com.asimorphic.chat.domain.notification.DeviceTokenService
import com.asimorphic.core.data.network.delete
import com.asimorphic.core.data.network.post
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorDeviceTokenService(
    private val httpClient: HttpClient
): DeviceTokenService {
    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/notification/register",
            body = RegisterDeviceTokenRequest(
                token = token,
                platform = platform
            )
        )
    }

    override suspend fun deregisterToken(token: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/notification/deregister/$token"
        )
    }
}