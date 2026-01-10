package com.asimorphic.core.data.mapper

import com.asimorphic.core.data.dto.UserDto
import com.asimorphic.core.domain.model.User

fun UserDto.toModel(): User {
    return User(
        id = id,
        username = username,
        email = email,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        username = username,
        email = email,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}