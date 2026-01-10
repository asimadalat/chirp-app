package com.asimorphic.core.domain.auth

import com.asimorphic.core.domain.model.AuthCredential
import kotlinx.coroutines.flow.Flow

interface SessionService {
    fun observeAuthCredential(): Flow<AuthCredential?>
    suspend fun set(credential: AuthCredential?)
}