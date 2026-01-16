package com.asimorphic.core.data.auth

import com.asimorphic.core.data.dto.AuthCredentialDto
import com.asimorphic.core.data.dto.request.EmailRequest
import com.asimorphic.core.data.dto.request.LoginRequest
import com.asimorphic.core.data.dto.request.RegisterRequest
import com.asimorphic.core.data.mapper.toModel
import com.asimorphic.core.data.network.get
import com.asimorphic.core.data.network.post
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.model.AuthCredential
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result
import com.asimorphic.core.domain.util.map
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

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthCredential, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthCredentialDto>(
            route = "api/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map { authCredentialDto ->
            authCredentialDto.toModel()
        }
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

    override suspend fun forgotPassword(
        email: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post<EmailRequest, Unit>(
            route = "/api/auth/forgot-password",
            body = EmailRequest(email = email)
        )
    }
}