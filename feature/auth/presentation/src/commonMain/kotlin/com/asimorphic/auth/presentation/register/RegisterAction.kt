package com.asimorphic.auth.presentation.register

sealed interface RegisterAction {
    data object OnInputTextFocus: RegisterAction
    data object OnTogglePasswordVisibilityClick: RegisterAction
    data object OnRegisterClick: RegisterAction
    data object OnLoginClick: RegisterAction
}