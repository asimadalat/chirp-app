package com.asimorphic.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthCredentialDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)
