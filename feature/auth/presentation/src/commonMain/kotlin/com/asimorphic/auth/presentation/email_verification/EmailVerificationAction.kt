package com.asimorphic.auth.presentation.email_verification

sealed interface EmailVerificationAction {
    data object OnLoginClick: EmailVerificationAction
    data object OnCloseClick: EmailVerificationAction
}