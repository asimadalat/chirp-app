package com.asimorphic.auth.presentation.reset_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.confirm_new_password
import chirp.feature.auth.presentation.generated.resources.create_a_new_password
import chirp.feature.auth.presentation.generated.resources.new_password
import chirp.feature.auth.presentation.generated.resources.password_hint
import chirp.feature.auth.presentation.generated.resources.password_placeholder
import chirp.feature.auth.presentation.generated.resources.reset_password_success
import chirp.feature.auth.presentation.generated.resources.submit
import com.asimorphic.core.designsystem.component.brand.ChirpLogoBrand
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.layout.ChirpFormLayout
import com.asimorphic.core.designsystem.component.layout.ChirpSnackbarScaffold
import com.asimorphic.core.designsystem.component.text_field.ChirpPasswordTextField
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordRoot(
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResetPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
) {
    ChirpSnackbarScaffold {
        ChirpFormLayout(
            headerText = stringResource(
                resource = Res.string.create_a_new_password
            ),
            errorText = state.errorText?.asString(),
            logo = {
                ChirpLogoBrand()
            }
        ) {
            ChirpPasswordTextField(
                state = state.passwordTextFieldState,
                title = stringResource(resource = Res.string.new_password),
                placeholder = stringResource(resource = Res.string.password_placeholder),
                supportingText = stringResource(resource = Res.string.password_hint),
                onToggleVisibilityClick = {
                    onAction(ResetPasswordAction.OnTogglePasswordVisibilityClick)
                },
                isPasswordVisible = state.isPasswordVisible
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            ChirpPasswordTextField(
                state = state.confirmPasswordTextFieldState,
                title = stringResource(resource = Res.string.confirm_new_password),
                placeholder = stringResource(resource = Res.string.password_placeholder),
                onToggleVisibilityClick = {
                    onAction(ResetPasswordAction.OnToggleConfirmPasswordVisibilityClick)
                },
                isPasswordVisible = state.isConfirmPasswordVisible
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            ChirpButton(
                text = stringResource(resource = Res.string.submit),
                enabled = !state.isLoading && state.canSubmit,
                isLoading = state.isLoading,
                onClick = {
                    onAction(ResetPasswordAction.OnSubmitClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (state.isResetSuccess) {
                Spacer(modifier = Modifier.height(height = 10.dp))
                Text(
                    text = stringResource(resource = Res.string.reset_password_success),
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
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {}
        )
    }
}