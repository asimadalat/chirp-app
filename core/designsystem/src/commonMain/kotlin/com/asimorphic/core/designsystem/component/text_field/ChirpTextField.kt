package com.asimorphic.core.designsystem.component.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    startIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
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
        BasicTextField(
            state = state,
            enabled = enabled,
            lineLimits = if (singleLine) {
                TextFieldLineLimits.SingleLine
            } else {
                TextFieldLineLimits.Default
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textPlaceholder
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            cursorBrush = SolidColor(
                value = MaterialTheme.colorScheme.onSurface
            ),
            interactionSource = interactionSource,
            modifier = styleModifier,
            decorator = { innerBox ->
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
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChirpTextFieldEmptyPreview() {
    ChirpTheme {
        ChirpTextField(
            modifier = Modifier
                .width(width = 300.dp),
            state = rememberTextFieldState(),
            placeholder = "sample@test.com",
            title = "Email",
            supportingText = "Please enter your email."
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChirpTextFieldPopulatedPreview() {
    ChirpTheme {
        ChirpTextField(
            modifier = Modifier
                .width(width = 300.dp),
            state = rememberTextFieldState(
                initialText = "sample@test.com"
            ),
            placeholder = "sample@test.com",
            title = "Email",
            supportingText = "Please enter your email."
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChirpTextFieldDisabledPreview() {
    ChirpTheme {
        ChirpTextField(
            modifier = Modifier
                .width(width = 300.dp),
            state = rememberTextFieldState(),
            enabled = false,
            placeholder = "sample@test.com",
            title = "Email",
            supportingText = "Please enter your email."
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChirpTextFieldErrorPreview() {
    ChirpTheme {
        ChirpTextField(
            modifier = Modifier
                .width(width = 300.dp),
            state = rememberTextFieldState(),
            isError = true,
            placeholder = "sample@test.com",
            title = "Email",
            supportingText = "Please enter a valid email."
        )
    }
}

