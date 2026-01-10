package com.asimorphic.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.account_successfully_created
import chirp.feature.auth.presentation.generated.resources.login
import chirp.feature.auth.presentation.generated.resources.resend_verification_email
import chirp.feature.auth.presentation.generated.resources.resend_verification_email_success
import chirp.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import com.asimorphic.core.designsystem.component.brand.ChirpSuccessIconBrand
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.layout.ChirpResultLayout
import com.asimorphic.core.designsystem.component.layout.ChirpSnackbarScaffold
import com.asimorphic.core.designsystem.component.layout.ChirpSuccessLayout
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterSuccessRoot(
    viewModel: RegisterSuccessViewModel = koinViewModel(),
    onLoginClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is RegisterSuccessEvent.ResendVerificationEmailSuccess ->
                snackbarHostState.showSnackbar(
                    message = getString(resource = Res.string.resend_verification_email_success)
                )
        }
    }
    RegisterSuccessScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is RegisterSuccessAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action = action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterSuccessScreen(
    state: RegisterSuccessState,
    onAction: (RegisterSuccessAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    ChirpSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        ChirpResultLayout {
            ChirpSuccessLayout(
                title = stringResource(resource = Res.string.account_successfully_created),
                description = stringResource(
                    resource = Res.string.verification_email_sent_to_x,
                    state.emailRegistered
                ),
                icon = {
                    ChirpSuccessIconBrand()
                },
                primaryButton = {
                    ChirpButton(
                        text = stringResource(resource = Res.string.login),
                        onClick = {
                            onAction(RegisterSuccessAction.OnLoginClick)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                secondaryButton = {
                    ChirpButton(
                        text = stringResource(resource = Res.string.resend_verification_email),
                        onClick = {
                            onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isResendingVerificationEmail,
                        type = ChirpButtonType.SECONDARY,
                        isLoading = state.isResendingVerificationEmail
                    )
                },
                secondaryError = state.resendVerificationError?.asString()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}