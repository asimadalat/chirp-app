package com.asimorphic.auth.presentation.register_success

import com.asimorphic.core.presentation.util.UiText

data class RegisterSuccessState(
    val emailRegistered: String = "",
    val isResendingVerificationEmail: Boolean = false,
    val resendVerificationError: UiText? = null
)