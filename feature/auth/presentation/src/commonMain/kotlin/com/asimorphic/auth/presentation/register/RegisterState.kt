package com.asimorphic.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.core.presentation.util.UiText

data class RegisterState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val isValidEmail: Boolean = false,
    val emailError: UiText? = null,

    val usernameTextFieldState: TextFieldState = TextFieldState(),
    val isValidUsername: Boolean = false,
    val usernameError: UiText? = null,

    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isValidPassword: Boolean = false,
    val passwordError: UiText? = null,

    val registrationError: UiText? = null,
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false
)