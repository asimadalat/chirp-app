package com.asimorphic.core.domain.validate

object PasswordValidator
{
    private const val MIN_PASSWORD_LENGTH = 8

    fun validate(password: String): PasswordValidationState {
        return PasswordValidationState(
            hasMinLength = password.length >= MIN_PASSWORD_LENGTH,
            hasDigit = password.any { it.isDigit() },
            hasUppercase = password.any { it.isUpperCase() }
        )
    }
}