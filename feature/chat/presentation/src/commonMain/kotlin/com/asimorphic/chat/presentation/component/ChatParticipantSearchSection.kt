package com.asimorphic.chat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.add
import chirp.feature.chat.presentation.generated.resources.email_or_username
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.text_field.ChirpTextField
import com.asimorphic.core.presentation.util.UiText
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatParticipantSearchSection(
    queryState: TextFieldState,
    isLoading: Boolean,
    isSearchEnabled: Boolean,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    onFocusChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(
            horizontal = 20.dp,
            vertical = 16.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        ChirpTextField(
            state = queryState,
            modifier = Modifier.weight(weight = 1f),
            title = null,
            placeholder = stringResource(resource = Res.string.email_or_username),
            isError = error != null,
            supportingText = error?.asString(),
            keyboardType = KeyboardType.Email,
            singleLine = true,
            onFocusChanged = onFocusChanged
        )
        ChirpButton(
            text = stringResource(resource = Res.string.add),
            type = ChirpButtonType.SECONDARY,
            onClick = onAddClick,
            enabled = isSearchEnabled,
            isLoading = isLoading
        )
    }
}