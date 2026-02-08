package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimorphic.chat.domain.model.ChatMessageDeliveryStatus
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.chat.presentation.util.getChatBubbleColourForUser
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.presentation.util.UiText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatDetailListItemUi(
    messageUi: MessageUi,
    onMessageLongClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: (MessageUi.SelfParticipantMessage) -> Unit,
    onRetryClick: (MessageUi.SelfParticipantMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when (messageUi) {
            is MessageUi.DateSeparator -> {
                DateSeparatorUi(
                    date = messageUi.date.asString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is MessageUi.OtherParticipantMessage -> {
                OtherParticipantMessageUi(
                    message = messageUi,
                    color = getChatBubbleColourForUser(
                        userId = messageUi.sender.id
                    )
                )
            }
            is MessageUi.SelfParticipantMessage -> {
                SelfParticipantMessageUi(
                    message = messageUi,
                    onMessageLongClick = {
                        onMessageLongClick(messageUi)
                    },
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteClick =  {
                        onDeleteClick(messageUi)
                    },
                    onRetryClick = {
                        onRetryClick(messageUi)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun ChatDetailListItemSelfMessageSentUiPreview() {
    ChirpTheme {
        ChatDetailListItemUi(
            messageUi = MessageUi.SelfParticipantMessage(
                id = "0",
                content = "The quick brown fox jumped over the lazy dog.",
                formattedSentTime = UiText.DynamicString(value="Tuesday 7:43pm"),
                deliveryStatus = ChatMessageDeliveryStatus.SENT,
                isMenuOpen = true
            ),
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            modifier = Modifier.fillMaxWidth()
                .height(height = 200.dp)
        )
    }
}

@Composable
@Preview
fun ChatDetailListItemSelfMessageFailedUiPreview() {
    ChirpTheme {
        ChatDetailListItemUi(
            messageUi = MessageUi.SelfParticipantMessage(
                id = "0",
                content = "The quick brown fox jumped over the lazy dog.",
                formattedSentTime = UiText.DynamicString(value="Tuesday 7:43pm"),
                deliveryStatus = ChatMessageDeliveryStatus.FAILED,
                isMenuOpen = true
            ),
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            modifier = Modifier.fillMaxWidth()
                .height(height = 200.dp)
        )
    }
}

@Composable
@Preview
fun ChatDetailListItemOtherMessageUiPreview() {
    ChirpTheme {
        ChatDetailListItemUi(
            messageUi = MessageUi.OtherParticipantMessage(
                id = "0",
                content = "The quick brown fox jumped over the lazy dog.",
                formattedSentTime = UiText.DynamicString(value="Tuesday 7:43pm"),
                sender = ChatParticipantUi(
                    id = "1",
                    username = "John",
                    initials = "JD"
                )
            ),
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            modifier = Modifier.fillMaxWidth()
                .height(height = 200.dp)
        )
    }
}