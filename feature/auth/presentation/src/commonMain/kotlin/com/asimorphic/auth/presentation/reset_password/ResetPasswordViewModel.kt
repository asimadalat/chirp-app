package com.asimorphic.auth.presentation.reset_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_cannot_reuse_password
import chirp.feature.auth.presentation.generated.resources.error_email_link_expired_or_invalid
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.domain.util.onFailure
import com.asimorphic.core.domain.util.onSuccess
import com.asimorphic.core.domain.validate.PasswordValidator
import com.asimorphic.core.presentation.mapper.toUiText
import com.asimorphic.core.presentation.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val token = savedStateHandle.get<String>("token")
        ?: throw IllegalStateException(
            "Password reset token required for operation"
        )

    private val _state = MutableStateFlow(ResetPasswordState())
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
            initialValue = ResetPasswordState()
        )

    private val passwordFlow = snapshotFlow { state.value.passwordTextFieldState.text.toString() }
        .map { password -> PasswordValidator.validate(password = password).isValidPassword }
        .distinctUntilChanged()

    private val confirmPasswordFlow = snapshotFlow { state.value.confirmPasswordTextFieldState.text.toString() }
        .map { password -> PasswordValidator.validate(password = password).isValidPassword }
        .distinctUntilChanged()

    private fun observeValidationState() {
        combine(
            flow = passwordFlow,
            flow2 = confirmPasswordFlow
        ) { passwordValid, confirmPasswordValid ->
            val allValid = passwordValid && confirmPasswordValid
            _state.update { it.copy(
                canSubmit = allValid
            ) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            ResetPasswordAction.OnSubmitClick -> submit()
            ResetPasswordAction.OnToggleConfirmPasswordVisibilityClick -> {
                _state.update { it.copy(
                    isConfirmPasswordVisible = !it.isConfirmPasswordVisible
                ) }
            }
            ResetPasswordAction.OnTogglePasswordVisibilityClick -> {
                _state.update { it.copy(
                    isPasswordVisible = !it.isPasswordVisible
                ) }
            }
        }
    }

    private fun submit() {
        if (state.value.isLoading || !state.value.canSubmit)
            return

        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true,
                isResetSuccess = false,
                errorText = null
            ) }

            val newPassword = state.value.passwordTextFieldState.text.toString()
            authService.resetPassword(
                token = token,
                newPassword = newPassword
            ).onSuccess {
                    _state.update { it.copy(
                        isResetSuccess = true,
                        isLoading = false
                    ) }
                }
                .onFailure { dataErrorRemote ->
                    val errorText = when (dataErrorRemote) {
                        DataError.Remote.UNAUTHORIZED -> UiText.Resource(
                            id = Res.string.error_email_link_expired_or_invalid
                        )
                        DataError.Remote.CONFLICT -> UiText.Resource(
                            id = Res.string.error_cannot_reuse_password
                        )
                        else -> dataErrorRemote.toUiText()
                    }
                    _state.update { it.copy(
                        errorText = errorText,
                        isLoading = false
                    ) }
                }
        }
    }
}