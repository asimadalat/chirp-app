package com.asimorphic.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.create_account
import chirp.feature.auth.presentation.generated.resources.email
import chirp.feature.auth.presentation.generated.resources.email_placeholder
import chirp.feature.auth.presentation.generated.resources.forgot_password
import chirp.feature.auth.presentation.generated.resources.login
import chirp.feature.auth.presentation.generated.resources.password
import chirp.feature.auth.presentation.generated.resources.password_placeholder
import chirp.feature.auth.presentation.generated.resources.welcome_back
import com.asimorphic.core.designsystem.component.brand.ChirpLogoBrand
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.layout.ChirpFormLayout
import com.asimorphic.core.designsystem.component.layout.ChirpSnackbarScaffold
import com.asimorphic.core.designsystem.component.text_field.ChirpPasswordTextField
import com.asimorphic.core.designsystem.component.text_field.ChirpTextField
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            LoginEvent.Success -> onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnCreateAccountClick -> onCreateAccountClick()
                LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                else -> Unit
            }
            viewModel.onAction(action = action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    ChirpSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        ChirpFormLayout(
            headerText = stringResource(resource = Res.string.welcome_back),
            errorText = state.error?.asString(),
            logo = {
                ChirpLogoBrand()
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChirpTextField(
                state = state.emailTextFieldState,
                placeholder = stringResource(resource = Res.string.email_placeholder),
                title = stringResource(resource = Res.string.email),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(height = 18.dp))
            ChirpPasswordTextField(
                state = state.passwordTextFieldState,
                placeholder = stringResource(resource = Res.string.password_placeholder),
                title = stringResource(resource = Res.string.password),
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(height = 10.dp))
            Text(
                text = stringResource(resource = Res.string.forgot_password),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .clickable {
                        onAction(LoginAction.OnForgotPasswordClick)
                    }
            )
            Spacer(modifier = Modifier.height(height = 24.dp))
            ChirpButton(
                text = stringResource(resource = Res.string.login),
                type = ChirpButtonType.PRIMARY,
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                },
                enabled = state.canLogIn,
                isLoading = state.isLoggingIn,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(height = 10.dp))
            ChirpButton(
                text = stringResource(resource = Res.string.create_account),
                type = ChirpButtonType.SECONDARY,
                onClick = {
                    onAction(LoginAction.OnCreateAccountClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun LightModePreview() {
    ChirpTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview
@Composable
private fun DarkModePreview() {
    ChirpTheme(darkMode = true) {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}