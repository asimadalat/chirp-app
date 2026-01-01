package com.asimorphic.core.domain.auth

import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.EmptyResult

interface AuthService {
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<DataError.Remote>
}