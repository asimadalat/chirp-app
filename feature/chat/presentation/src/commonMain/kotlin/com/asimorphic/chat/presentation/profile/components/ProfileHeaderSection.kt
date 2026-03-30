package com.asimorphic.chat.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.cancel
import chirp.feature.chat.presentation.generated.resources.profile_settings
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.button.ChirpIconButton
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileHeaderSection(
    username: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = username,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(Res.string.profile_settings),
                color = MaterialTheme.colorScheme.extended.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        ChirpIconButton(
            onClick = onCloseClick,
            type = ChirpButtonType.SECONDARY
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(Res.string.cancel),
                tint = MaterialTheme.colorScheme.extended.textSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}