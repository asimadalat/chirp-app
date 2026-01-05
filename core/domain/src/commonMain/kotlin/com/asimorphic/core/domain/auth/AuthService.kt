package com.asimorphic.core.domain.auth

import com.asimorphic.core.domain.model.AuthCredential
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult
import com.asimorphic.core.domain.util.Result

interface AuthService {
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<DataError.Remote>

    suspend fun login(
        email: String,
        password: String
    ): Result<AuthCredential, DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote>

    suspend fun verifyEmail(
        token: String
    ): EmptyResult<DataError.Remote>
}