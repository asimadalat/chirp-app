package com.asimorphic.core.data.mapper

import com.asimorphic.core.data.dto.AuthCredentialDto
import com.asimorphic.core.domain.model.AuthCredential

fun AuthCredentialDto.toModel(): AuthCredential {
    return AuthCredential(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toModel()
    )
}

fun AuthCredential.toDto(): AuthCredentialDto {
    return AuthCredentialDto(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toDto()
    )
}