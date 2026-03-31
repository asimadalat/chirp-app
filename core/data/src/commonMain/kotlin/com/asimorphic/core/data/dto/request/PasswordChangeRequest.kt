package com.asimorphic.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class PasswordChangeRequest(
    val oldPassword: String,
    val newPassword: String
)
