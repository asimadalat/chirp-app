package com.asimorphic.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_email_not_verified
import chirp.feature.auth.presentation.generated.resources.error_invalid_credentials
import com.asimorphic.auth.domain.EmailValidator
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
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

class LoginViewModel(
    private val authService: AuthService,
    private val sessionService: SessionService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    private val emailFlow = snapshotFlow { state.value.emailTextFieldState.text.toString() }
        .map { email -> EmailValidator.validate(email = email) }
        .distinctUntilChanged()

    private val passwordNotBlankFlow = snapshotFlow { state.value.passwordTextFieldState.text.toString() }
        .map { it.isNotBlank() }
        .distinctUntilChanged()

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeTextFieldStates()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    private val isLoggingInFlow = state
        .map { it.isLoggingIn }
        .distinctUntilChanged()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibility -> {
                _state.update { it.copy(
                    isPasswordVisible = !it.isPasswordVisible
                ) }
            }
            else -> Unit
        }
    }

    private fun login() {
        if (!state.value.canLogIn)
            return

        viewModelScope.launch {
            _state.update { it.copy(
                isLoggingIn = true
            ) }

            val email = state.value.emailTextFieldState.text.toString()
            val password = state.value.passwordTextFieldState.text.toString()

            authService.login(
                email = email,
                password = password
            ).onSuccess { authCredential ->
                sessionService.set(authCredential)

                _state.update { it.copy(
                    isLoggingIn = false
                ) }

                eventChannel.send(element = LoginEvent.Success)
            }.onFailure { dataErrorRemote ->
                val errorMessage = when(dataErrorRemote) {
                    DataError.Remote.UNAUTHORIZED -> UiText.Resource(id = Res.string.error_invalid_credentials)
                    DataError.Remote.FORBIDDEN -> UiText.Resource(id = Res.string.error_email_not_verified)
                    else -> dataErrorRemote.toUiText()
                }

                _state.update { it.copy(
                    isLoggingIn = false,
                    error = errorMessage
                ) }
            }
        }
    }

    private fun observeTextFieldStates() {
        combine(
            emailFlow,
            passwordNotBlankFlow,
            isLoggingInFlow
        ) { isEmailValid, isPasswordNotBlank, isLoggingIn ->
            _state.update { it.copy(
                canLogIn = !isLoggingIn && isEmailValid && isPasswordNotBlank
            ) }
        }.launchIn(scope = viewModelScope)
    }
}