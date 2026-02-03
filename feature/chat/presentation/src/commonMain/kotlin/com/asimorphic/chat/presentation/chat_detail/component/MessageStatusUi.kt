package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.check_icon
import chirp.feature.chat.presentation.generated.resources.failed
import chirp.feature.chat.presentation.generated.resources.loading_icon
import chirp.feature.chat.presentation.generated.resources.sending
import chirp.feature.chat.presentation.generated.resources.sent
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.designsystem.theme.labelXSmall
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MessageStatus(
    status: ChatMessageDeliveryStatus,
    modifier: Modifier = Modifier
) {
    val (text, icon, colour) = when (status) {
        ChatMessageDeliveryStatus.SENDING -> Triple(
            first = stringResource(resource = Res.string.sending),
            second = vectorResource(resource = Res.drawable.loading_icon),
            third = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChatMessageDeliveryStatus.SENT -> Triple(
            first = stringResource(resource = Res.string.sent),
            second = vectorResource(resource = Res.drawable.check_icon),
            third = MaterialTheme.colorScheme.extended.textTertiary
        )
        ChatMessageDeliveryStatus.FAILED -> Triple(
            first = stringResource(resource = Res.string.failed),
            second = Icons.Rounded.Close,
            third = MaterialTheme.colorScheme.error
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = colour,
            modifier = Modifier.size(size = 14.dp)
        )
        Spacer(modifier = Modifier.width(width = 4.dp))
        Text(
            text = text,
            color = colour,
            style = MaterialTheme.typography.labelXSmall
        )
    }
}