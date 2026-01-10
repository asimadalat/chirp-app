package com.asimorphic.auth.presentation.email_verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.close
import chirp.feature.auth.presentation.generated.resources.email_verified_failure
import chirp.feature.auth.presentation.generated.resources.email_verified_failure_desc
import chirp.feature.auth.presentation.generated.resources.email_verified_success
import chirp.feature.auth.presentation.generated.resources.email_verified_success_desc
import chirp.feature.auth.presentation.generated.resources.login
import chirp.feature.auth.presentation.generated.resources.verifying_account
import com.asimorphic.core.designsystem.component.brand.ChirpFailureIconBrand
import com.asimorphic.core.designsystem.component.brand.ChirpSuccessIconBrand
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.layout.ChirpResultLayout
import com.asimorphic.core.designsystem.component.layout.ChirpSuccessLayout
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerificationRoot(
    viewModel: EmailVerificationViewModel = koinViewModel(),
    onLoginClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmailVerificationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmailVerificationAction.OnLoginClick -> onLoginClick()
                EmailVerificationAction.OnCloseClick -> onCloseClick()
            }
            viewModel.onAction(action = action)
        }
    )
}

@Composable
fun EmailVerificationScreen(
    state: EmailVerificationState,
    onAction: (EmailVerificationAction) -> Unit,
) {
    ChirpResultLayout {
        when {
            state.isVerifying -> {
                VerifyingLayout()
            }
            state.isVerified -> {
                ChirpSuccessLayout(
                    title = stringResource(resource = Res.string.email_verified_success),
                    description = stringResource(resource = Res.string.email_verified_success_desc),
                    icon = {
                        ChirpSuccessIconBrand()
                    },
                    primaryButton = {
                        ChirpButton(
                            text = stringResource(resource = Res.string.login),
                            onClick = {
                                onAction(EmailVerificationAction.OnLoginClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                )
            }
            else -> {
                ChirpSuccessLayout(
                    title = stringResource(resource = Res.string.email_verified_failure),
                    description = stringResource(resource = Res.string.email_verified_failure_desc),
                    icon = {
                        Spacer(modifier = Modifier.height(height = 34.dp))
                        ChirpFailureIconBrand(
                            modifier = Modifier
                                .size(size = 80.dp)
                        )
                        Spacer(modifier = Modifier.height(height = 34.dp))
                    },
                    primaryButton = {
                        ChirpButton(
                            text = stringResource(resource = Res.string.close),
                            onClick = {
                                onAction(EmailVerificationAction.OnCloseClick)
                            },
                            type = ChirpButtonType.SECONDARY,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun VerifyingLayout(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = 200.dp)
            .padding(all = 16.dp),
        verticalArrangement = Arrangement
            .spacedBy(
                space = 16.dp,
                Alignment.CenterVertically
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size = 60.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(resource = Res.string.verifying_account),
            color = MaterialTheme.colorScheme.extended.textSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun EmailVerifyingPreview() {
    ChirpTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun VerifySuccessPreview() {
    ChirpTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerified = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun VerifyFailurePreview() {
    ChirpTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(),
            onAction = {}
        )
    }
}