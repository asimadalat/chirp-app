package com.asimorphic.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String? = null
)
