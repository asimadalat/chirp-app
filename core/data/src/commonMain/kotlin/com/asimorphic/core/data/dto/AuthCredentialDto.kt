package com.asimorphic.core.data.dto

data class AuthCredentialDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)
