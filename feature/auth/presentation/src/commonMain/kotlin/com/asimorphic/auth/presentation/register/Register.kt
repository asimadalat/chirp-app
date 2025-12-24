package com.asimorphic.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.email
import chirp.feature.auth.presentation.generated.resources.email_placeholder
import chirp.feature.auth.presentation.generated.resources.login
import chirp.feature.auth.presentation.generated.resources.password
import chirp.feature.auth.presentation.generated.resources.password_hint
import chirp.feature.auth.presentation.generated.resources.password_placeholder
import chirp.feature.auth.presentation.generated.resources.register
import chirp.feature.auth.presentation.generated.resources.username
import chirp.feature.auth.presentation.generated.resources.username_hint
import chirp.feature.auth.presentation.generated.resources.username_placeholder
import chirp.feature.auth.presentation.generated.resources.welcome_to_chirp
import com.asimorphic.core.designsystem.component.brand.ChirpLogoBrand
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.layout.ChirpFormLayout
import com.asimorphic.core.designsystem.component.layout.ChirpSnackbarScaffold
import com.asimorphic.core.designsystem.component.text_field.ChirpPasswordTextField
import com.asimorphic.core.designsystem.component.text_field.ChirpTextField
import com.asimorphic.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    ChirpSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        ChirpFormLayout(
            headerText = stringResource(resource = Res.string.welcome_to_chirp),
            errorText = state.registrationError?.asString(),
            logo = { ChirpLogoBrand() }
        ) {
            ChirpTextField(
                state = state.usernameTextFieldState,
                title = stringResource(resource = Res.string.username),
                placeholder = stringResource(resource = Res.string.username_placeholder),
                supportingText = state.usernameError?.asString()
                    ?: stringResource(resource = Res.string.username_hint),
                isError = state.usernameError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocus)
                }
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            ChirpTextField(
                state = state.emailTextFieldState,
                title = stringResource(resource = Res.string.email),
                placeholder = stringResource(resource = Res.string.email_placeholder),
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocus)
                }
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            ChirpPasswordTextField(
                state = state.passwordTextFieldState,
                title = stringResource(resource = Res.string.password),
                placeholder = stringResource(resource = Res.string.password_placeholder),
                supportingText = state.passwordError?.asString()
                    ?: stringResource(resource = Res.string.password_hint),
                isError = state.passwordError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocus)
                },
                onToggleVisibilityClick = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                isPasswordVisible = state.isPasswordVisible
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            ChirpButton(
                text = stringResource(resource = Res.string.register),
                enabled = state.canRegister,
                isLoading = state.isRegistering,
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(height = 10.dp))
            ChirpButton(
                text = stringResource(resource = Res.string.login),
                type = ChirpButtonType.SECONDARY,
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}