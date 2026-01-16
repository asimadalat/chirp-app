package com.asimorphic.auth.presentation.forgot_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.email
import chirp.feature.auth.presentation.generated.resources.email_placeholder
import chirp.feature.auth.presentation.generated.resources.forgot_password
import chirp.feature.auth.presentation.generated.resources.forgot_password_email_sent_success
import chirp.feature.auth.presentation.generated.resources.submit
import com.asimorphic.core.designsystem.component.brand.ChirpLogoBrand
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.layout.ChirpFormLayout
import com.asimorphic.core.designsystem.component.layout.ChirpSnackbarScaffold
import com.asimorphic.core.designsystem.component.text_field.ChirpTextField
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordRoot(
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
) {
    ChirpSnackbarScaffold {
        ChirpFormLayout(
            headerText = stringResource(
                resource = Res.string.forgot_password
            ),
            errorText = state.errorText?.asString(),
            logo = {
                ChirpLogoBrand()
            }
        ) {
            ChirpTextField(
                state = state.emailTextFieldState,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(
                    resource = Res.string.email
                ),
                placeholder = stringResource(
                    resource = Res.string.email_placeholder
                ),
                isError = state.errorText != null,
                supportingText = state.errorText?.asString(),
                keyboardType = KeyboardType.Email,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            ChirpButton(
                text = stringResource(resource = Res.string.submit),
                onClick = {
                    onAction(ForgotPasswordAction.OnSubmitClick)
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isLoading,
                enabled = !state.isLoading && state.canSubmit
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            if (state.isEmailSentSuccessfully) {
                Text(
                    text = stringResource(
                        resource = Res.string.forgot_password_email_sent_success
                    ),
                    color = MaterialTheme.colorScheme.extended.success,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}