package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.cloud_off_icon
import chirp.feature.chat.presentation.generated.resources.send
import chirp.feature.chat.presentation.generated.resources.send_a_message
import com.asimorphic.chat.domain.model.ConnectionState
import com.asimorphic.chat.presentation.mapper.toUiText
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.button.ChirpIconButton
import com.asimorphic.core.designsystem.component.text_field.ChirpMultiLineTextField
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageEntryBox(
    messageTextFieldState: TextFieldState,
    connectionState: ConnectionState,
    isTextInputEnabled: Boolean,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = connectionState == ConnectionState.CONNECTED

    ChirpMultiLineTextField(
        state = messageTextFieldState,
        modifier = modifier,
        enabled = isTextInputEnabled,
        placeholder = stringResource(
            resource = Res.string.send_a_message
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send
        ),
        onKeyboardAction = onSendClick,
    ) {
        Spacer(modifier = Modifier.weight(weight = 1f))

        if (!isConnected) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = vectorResource(
                        resource = Res.drawable.cloud_off_icon
                    ),
                    contentDescription = connectionState.toUiText().asString(),
                    tint = MaterialTheme.colorScheme.extended.textDisabled,
                    modifier = Modifier.size(size = 16.dp)
                )
                Text(
                    text = connectionState.toUiText().asString(),
                    color = MaterialTheme.colorScheme.extended.textDisabled,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        ChirpIconButton(
            onClick = {},
            type = ChirpButtonType.PRIMARY,
            enabled = isConnected && isTextInputEnabled
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(
                    resource = Res.string.send
                )
            )
        }
    }
}

@Composable
@Preview
fun MessageEntryBoxPreview() {
    ChirpTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 300.dp),
        ) {
            MessageEntryBox(
                messageTextFieldState = rememberTextFieldState(),
                connectionState = ConnectionState.CONNECTING,
                isTextInputEnabled = true,
                onSendClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}