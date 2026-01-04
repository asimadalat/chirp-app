package com.asimorphic.auth.presentation.register_success

data class RegisterSuccessState(
    val emailRegistered: String = "",
    val isResendingVerificationEmail: Boolean = false
)