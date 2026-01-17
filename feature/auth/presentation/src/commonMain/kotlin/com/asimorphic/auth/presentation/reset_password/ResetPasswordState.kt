package com.asimorphic.auth.presentation.reset_password

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.core.presentation.util.UiText

data class ResetPasswordState(
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val confirmPasswordTextFieldState: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val canSubmit: Boolean = false,
    val isResetSuccess: Boolean = false
)