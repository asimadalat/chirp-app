package com.asimorphic.core.designsystem.component.text_field

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpPasswordTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    isPasswordVisible: Boolean,
    onToggleVisibilityClick: () -> Unit,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {}
) {
    ChirpTextFieldLayout(
        title = title,
        isError = isError,
        supportingText = supportingText,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
        modifier = modifier
    ) { styleModifier, interactionSource ->
        BasicSecureTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            textObfuscationMode = if (isPasswordVisible) {
                TextObfuscationMode.Visible
            } else TextObfuscationMode.Hidden,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textPlaceholder
                }
            ),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onSurface),
            decorator = { innerBox ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.text.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.extended.textPlaceholder,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        innerBox()
                    }

//                    TODO: Add show/hide password drawable vectors
//                    Icon(
//                        imageVector = if (isPasswordVisible) {
//
//                        } else {
//
//                        },
//                        contentDescription = if (isPasswordVisible) {
//                            stringResource(resource = Res.string.hide_password)
//                        } else {
//                            stringResource(resource = Res.string.show_password)
//                        },
//                        tint = MaterialTheme.colorScheme.extended.textDisabled,
//                        modifier = Modifier
//                            .size(size = 20.dp)
//                            .clickable(
//                                interactionSource = remember { MutableInteractionSource() },
//                                indication = ripple(
//                                    bounded = false,
//                                    radius = 20.dp
//                                ),
//                                onClick = onToggleVisibilityClick
//                            )
//                    )
                }
            }
        )

    }
}

@Composable
@Preview(showBackground = true)
fun ChirpPasswordTextFieldEmptyPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            modifier = Modifier
                .width(width = 300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            state = rememberTextFieldState(),
            placeholder = "MyPassword123",
            title = "Email",
            supportingText = "Please enter your password."
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChirpPasswordTextFieldFilledPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            modifier = Modifier
                .width(width = 300.dp),
            isPasswordVisible = false,
            onToggleVisibilityClick = {},
            state = rememberTextFieldState(
                initialText = "Hello123!"
            ),
            placeholder = "sample@test.com",
            title = "Email",
            supportingText = "Please enter your password."
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChirpPasswordTextFieldErrorPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            modifier = Modifier
                .width(width = 300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            state = rememberTextFieldState(
                initialText = "Hello123!"
            ),
            placeholder = "sample@test.com",
            title = "Email",
            supportingText = "Password must have a special character.",
            isError = true
        )
    }
}
