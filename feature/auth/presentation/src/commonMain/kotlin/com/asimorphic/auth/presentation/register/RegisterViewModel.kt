package com.asimorphic.auth.presentation.register

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_account_exists
import chirp.feature.auth.presentation.generated.resources.error_invalid_email
import chirp.feature.auth.presentation.generated.resources.error_invalid_password
import chirp.feature.auth.presentation.generated.resources.error_invalid_username
import com.asimorphic.auth.domain.EmailValidator
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import com.asimorphic.core.domain.validate.PasswordValidator
import com.asimorphic.core.presentation.mapper.toUiText
import com.asimorphic.core.presentation.util.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidationState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )

    private val usernameFlow = snapshotFlow { state.value.usernameTextFieldState.text.toString() }
        .map { username -> username.length in 4..20 }
        .distinctUntilChanged()
    private val emailFlow = snapshotFlow { state.value.emailTextFieldState.text.toString() }
        .map { email -> EmailValidator.validate(email = email) }
        .distinctUntilChanged()
    private val passwordFlow = snapshotFlow { state.value.passwordTextFieldState.text.toString() }
        .map { password -> PasswordValidator.validate(password = password).isValidPassword }
        .distinctUntilChanged()
    private val isRegisteringFlow = state
        .map { it.isRegistering }
        .distinctUntilChanged()

    private fun observeValidationState() {
        combine(
            flow = usernameFlow,
            flow2 = emailFlow,
            flow3 = passwordFlow,
            flow4 = isRegisteringFlow
        ) { usernameValid, emailValid, passwordValid, isRegistering ->
            val allValid = usernameValid && emailValid && passwordValid
            _state.update { it.copy(
                canRegister = !isRegistering && allValid
            ) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                _state.update { it.copy(
                    isPasswordVisible = !it.isPasswordVisible
                ) }
            }
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnLoginClick -> Unit
            else -> Unit
        }
    }

    private fun register() {
        if (!validateFormInputs())
            return

        viewModelScope.launch {
            _state.update { it.copy(
                isRegistering = true
            ) }

            val username = state.value.usernameTextFieldState.text.toString()
            val email = state.value.emailTextFieldState.text.toString()
            val password = state.value.passwordTextFieldState.text.toString()

            authService.register(
                username = username,
                email = email,
                password = password
            ).onSuccess {
                _state.update { it.copy(
                    isRegistering = false
                ) }
                eventChannel.send(element = RegisterEvent.Success(email))
            }.onFailure { error ->
                val registrationError = when (error) {
                    DataError.Remote.CONFLICT -> UiText.Resource(id = Res.string.error_account_exists)
                    else-> error.toUiText()
                }

                _state.update { it.copy(
                    isRegistering = false,
                    registrationError = registrationError
                ) }
            }
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