package com.asimorphic.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_invalid_email
import chirp.feature.auth.presentation.generated.resources.error_invalid_password
import chirp.feature.auth.presentation.generated.resources.error_invalid_username
import com.asimorphic.auth.domain.EmailValidator
import com.asimorphic.core.domain.validate.PasswordValidator
import com.asimorphic.core.presentation.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnLoginClick -> validateFormInputs()
            else -> Unit
        }
    }

    private fun clearTextFieldErrors() {
        _state.update { it.copy(
            usernameError = null,
            emailError = null,
            passwordError = null,
            registrationError = null
        ) }
    }

    private fun validateFormInputs(): Boolean {
        clearTextFieldErrors()

        val currentState = state.value
        val username = currentState.usernameTextFieldState.text.toString()
        val email = currentState.emailTextFieldState.text.toString()
        val password = currentState.passwordTextFieldState.text.toString()

        val isValidUsername = username.length in 4..20
        val isValidEmail = EmailValidator.validate(email)
        val isValidPassword = PasswordValidator.validate(password).isValidPassword

        val usernameError = if (!isValidUsername)
            UiText.Resource(id = Res.string.error_invalid_username)
        else null
        val emailError = if (!isValidEmail)
            UiText.Resource(id = Res.string.error_invalid_email)
        else null
        val passwordError = if (!isValidPassword)
            UiText.Resource(id = Res.string.error_invalid_password)
        else null

        _state.update { it.copy(
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError
        ) }

        return isValidUsername && isValidEmail && isValidPassword
    }
}