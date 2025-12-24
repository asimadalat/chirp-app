package com.asimorphic.auth.domain

object EmailValidator {

    private const val EMAIL_PATTERN = "^([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)$"

    fun validate(email: String): Boolean {
        return EMAIL_PATTERN.toRegex().matches(input = email)
    }
}