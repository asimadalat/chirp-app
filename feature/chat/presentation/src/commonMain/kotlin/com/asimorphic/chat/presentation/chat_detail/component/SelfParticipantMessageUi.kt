package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.delete
import chirp.feature.chat.presentation.generated.resources.reload_icon
import chirp.feature.chat.presentation.generated.resources.retry
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.core.designsystem.component.chat.ChirpChatBubble
import com.asimorphic.core.designsystem.component.chat.TriangleAlign
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SelfParticipantMessageUi(
    message: MessageUi.SelfParticipantMessage,
    onMessageLongClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.End
        )
    ) {
        Box(
            modifier = Modifier.weight(weight = 1f)
        ) {
            ChirpChatBubble(
                from = "",
                messageBody = message.content,
                formattedTimestamp = message.formattedSentTime.asString(),
                triangleAlign = TriangleAlign.RIGHT,
                messageStatus = {
                    MessageStatus(status = message.deliveryStatus)
                },
                onLongClick = {
                    onMessageLongClick()
                }
            )

            DropdownMenu(
                expanded = message.isMenuOpen,
                onDismissRequest = onDismissMessageMenu,
                shape = RoundedCornerShape(size = 8.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.extended.surfaceOutline
                )
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(resource = Res.string.delete),
                            color = MaterialTheme.colorScheme.extended.destructiveHover,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick ={
                        onDismissMessageMenu()
                        onDeleteClick()
                    }
                )
            }
        }

        if (message.deliveryStatus == ChatMessageDeliveryStatus.FAILED) {
            IconButton(
                onClick = onRetryClick
            ) {
                Icon(
                    imageVector = vectorResource(resource = Res.drawable.reload_icon),
                    contentDescription = stringResource(Res.string.retry),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
