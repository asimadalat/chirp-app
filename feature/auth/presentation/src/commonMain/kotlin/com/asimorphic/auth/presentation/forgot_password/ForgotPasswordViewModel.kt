package com.asimorphic.auth.presentation.forgot_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asimorphic.auth.domain.EmailValidator
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import com.asimorphic.core.presentation.mapper.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authService: AuthService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val emailFlow = snapshotFlow { state.value.emailTextFieldState.text.toString() }
        .map { email -> EmailValidator.validate(email = email) }
        .distinctUntilChanged()

    private val _state = MutableStateFlow(ForgotPasswordState())
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
            initialValue = ForgotPasswordState()
        )

    private fun observeValidationState() {
        emailFlow.onEach { isValidEmail ->
            _state.update { it.copy(
                canSubmit = isValidEmail
            ) }
        }.launchIn(scope = viewModelScope)
    }

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnSubmitClick -> submit()
        }
    }

    private fun submit() {
        if (state.value.isLoading || !state.value.canSubmit)
            return

        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true,
                isEmailSentSuccessfully = false,
                errorText = null
            ) }

            val email = state.value.emailTextFieldState.text.toString()
            authService.forgotPassword(email)
                .onSuccess {
                    _state.update { it.copy(
                        isEmailSentSuccessfully = true,
                        isLoading = false
                    ) }
                }
                .onFailure { dataErrorRemote ->
                    _state.update { it.copy(
                        errorText = dataErrorRemote.toUiText(),
                        isLoading = false
                    ) }
                }
        }
    }
}