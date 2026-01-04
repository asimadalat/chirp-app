package com.asimorphic.core.designsystem.component.text_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.hide_password
import chirp.core.designsystem.generated.resources.hide_password_icon
import chirp.core.designsystem.generated.resources.show_password
import chirp.core.designsystem.generated.resources.show_password_icon
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
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
            modifier = styleModifier,
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
                        modifier = Modifier.weight(
                            weight = 1f
                        ),
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

                    Icon(
                        imageVector = if (isPasswordVisible) {
                            vectorResource(resource = Res.drawable.hide_password_icon)
                        } else {
                            vectorResource(resource = Res.drawable.show_password_icon)
                        },
                        contentDescription = if (isPasswordVisible) {
                            stringResource(resource = Res.string.hide_password)
                        } else {
                            stringResource(resource = Res.string.show_password)
                        },
                        tint = MaterialTheme.colorScheme.extended.textDisabled,
                        modifier = Modifier
                            .size(size = 22.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    bounded = false,
                                    radius = 20.dp
                                ),
                                onClick = onToggleVisibilityClick
                            )
                    )
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
