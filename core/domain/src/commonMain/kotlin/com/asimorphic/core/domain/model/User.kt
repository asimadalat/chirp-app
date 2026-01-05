package com.asimorphic.core.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String? = null
)
