package com.asimorphic.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.core.presentation.util.UiText

data class LoginState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val canLogIn: Boolean = false,
    val isLoggingIn: Boolean = false,
    val error: UiText? = null
)