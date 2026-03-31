package com.asimorphic.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import com.asimorphic.core.presentation.util.UiText

data class ProfileState(
    val username: String = "",
    val userInitials: String = "--",
    val profilePictureUrl: String? = null,
    val isUploadingImage: Boolean = false,
    val isDeletingImage: Boolean = false,
    val imageError: UiText? = null,
    val shouldShowDeleteConfirmationDialog: Boolean = false,
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val currentPasswordTextFieldState: TextFieldState = TextFieldState(),
    val newPasswordTextFieldState: TextFieldState = TextFieldState(),
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val canChangePassword: Boolean = false,
    val isChangingPassword: Boolean = false,
    val newPasswordError: UiText? = null,
    val isPasswordChangeSuccessful: Boolean = false
)