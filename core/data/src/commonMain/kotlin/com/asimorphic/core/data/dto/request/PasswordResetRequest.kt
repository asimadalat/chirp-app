package com.asimorphic.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val token: String,
    val newPassword: String
)
