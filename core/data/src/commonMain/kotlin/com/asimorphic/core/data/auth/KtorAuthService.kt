package com.asimorphic.core.data.auth

import com.asimorphic.core.data.dto.request.EmailRequest
import com.asimorphic.core.data.dto.request.RegisterRequest
import com.asimorphic.core.data.network.get
import com.asimorphic.core.data.network.post
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "api/auth/register",
            body = RegisterRequest(
                username = username,
                email = email,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/api/auth/resend-verification",
            body = EmailRequest(email = email)
        )
    }

    override suspend fun verifyEmail(
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/api/auth/verify-email",
            queryParams = mapOf("token" to token)
        )
    }
}