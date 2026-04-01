package com.asimorphic.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.error_incorrect_current_password
import chirp.feature.chat.presentation.generated.resources.error_invalid_file_type
import chirp.feature.chat.presentation.generated.resources.error_new_password_matches_current
import com.asimorphic.chat.domain.chat_participant.ChatParticipantRepository
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.auth.SessionService
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

class ProfileViewModel(
    private val authService: AuthService,
    private val sessionService: SessionService,
    private val chatParticipantRepository: ChatParticipantRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = combine(
        _state,
        sessionService.observeAuthCredential()
    ) { currentState, authCredential ->
        if (authCredential != null) {
            currentState.copy(
                username = authCredential.user.username,
                emailTextFieldState = TextFieldState(initialText = authCredential.user.email),
                profilePictureUrl = authCredential.user.profilePictureUrl
            )
        } else currentState
    }
        .onStart {
            if (!hasLoadedInitialData) {
                fetchSelfParticipant()
                observeCanChangePassword()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnChangePasswordClick -> changePassword()
            ProfileAction.OnToggleCurrentPasswordVisibility -> toggleCurrentPasswordVisibility()
            ProfileAction.OnToggleNewPasswordVisibility -> toggleNewPasswordVisibility()
            is ProfileAction.OnPictureSelected -> uploadProfilePicture(action.bytes, action.mimeType)
            ProfileAction.OnConfirmDeleteClick -> deleteProfilePicture()
            ProfileAction.OnDeletePictureClick -> showDeleteConfirmation()
            ProfileAction.OnDismissDeleteConfirmationDialogClick -> dismissDeleteConfirmation()
            else -> Unit
        }
    }

    private fun deleteProfilePicture() {
        if (state.value.isDeletingImage || state.value.profilePictureUrl == null)
            return

        _state.update { it.copy(
            shouldShowDeleteConfirmationDialog = false,
            isDeletingImage = true,
            imageError = null
        ) }
        viewModelScope.launch {
            chatParticipantRepository
                .deleteProfilePicture()
                .onSuccess {
                    _state.update { it.copy(
                        isDeletingImage = false
                    ) }
                }
                .onFailure { exception ->
                    _state.update { it.copy(
                        isDeletingImage = false,
                        imageError = exception.toUiText()
                    ) }
                }
        }

    }

    private fun dismissDeleteConfirmation() {
        _state.update { it.copy(
            shouldShowDeleteConfirmationDialog = false
        ) }
    }

    private fun showDeleteConfirmation() {
        _state.update { it.copy(
            shouldShowDeleteConfirmationDialog = true
        ) }
    }

    private fun toggleNewPasswordVisibility() {
        _state.update { it.copy(
            isNewPasswordVisible = !it.isNewPasswordVisible
        ) }
    }

    private fun toggleCurrentPasswordVisibility() {
        _state.update { it.copy(
            isCurrentPasswordVisible = !it.isCurrentPasswordVisible
        ) }
    }

    private fun uploadProfilePicture(bytes: ByteArray, mimeType: String?) {
        if (state.value.isUploadingImage)
            return

        if (mimeType == null) {
            _state.update { it.copy(
                imageError = UiText.Resource(Res.string.error_invalid_file_type)
            ) }
            return
        }

        _state.update { it.copy(
            isUploadingImage = true,
            imageError = null
        ) }
        viewModelScope.launch {
            chatParticipantRepository.uploadProfilePicture(
                imageBytes = bytes,
                mimeType = mimeType
            ).onSuccess {
                _state.update { it.copy(
                    isUploadingImage = false
                ) }
            }.onFailure { exception ->
                _state.update { it.copy(
                    imageError = exception.toUiText(),
                    isUploadingImage = false
                ) }
            }
        }
    }

    private fun observeCanChangePassword() {
        val isValidCurrentPasswordFlow = snapshotFlow {
            state.value.currentPasswordTextFieldState.text.toString()
        }.map {
            it.isNotBlank()
        }.distinctUntilChanged()
        val isValidNewPasswordFlow = snapshotFlow {
            state.value.newPasswordTextFieldState.text.toString()
        }.map {
            PasswordValidator.validate(it).isValidPassword
        }.distinctUntilChanged()

        combine(
            isValidCurrentPasswordFlow,
            isValidNewPasswordFlow
        ) { isCurrentValid, isNewValid ->
            _state.update { it.copy(
                canChangePassword = isCurrentValid && isNewValid
            ) }
        }.launchIn(
            scope = viewModelScope
        )
    }

    private fun fetchSelfParticipant() {
        viewModelScope.launch {
            chatParticipantRepository.fetchSelfParticipant()
        }
    }

    private fun changePassword() {
        if (!state.value.canChangePassword || state.value.isChangingPassword)
            return

        _state.update { it.copy(
            isChangingPassword = true,
            isPasswordChangeSuccessful = false
        ) }
        viewModelScope.launch {
            val currentPassword = state.value.currentPasswordTextFieldState.text.toString()
            val newPassword = state.value.newPasswordTextFieldState.text.toString()
            authService
                .changePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )
                .onSuccess {
                    state.value.currentPasswordTextFieldState.clearText()
                    state.value.newPasswordTextFieldState.clearText()
                    _state.update { it.copy(
                        isChangingPassword = false,
                        newPasswordError = null,
                        isPasswordChangeSuccessful = true,
                        isCurrentPasswordVisible = false,
                        isNewPasswordVisible = false
                    ) }
                }
                .onFailure { exception ->
                    val errorMessage = when (exception) {
                        DataError.Remote.UNAUTHORIZED -> {
                            UiText.Resource(Res.string.error_incorrect_current_password)
                        }
                        DataError.Remote.CONFLICT -> {
                            UiText.Resource(Res.string.error_new_password_matches_current)
                        }
                        else -> exception.toUiText()
                    }
                    _state.update { it.copy(
                        isChangingPassword = false,
                        newPasswordError = errorMessage
                    ) }
                }
        }

    }

}