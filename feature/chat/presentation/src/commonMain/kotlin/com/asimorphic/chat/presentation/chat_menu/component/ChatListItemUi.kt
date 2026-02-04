package com.asimorphic.chat.presentation.chat_menu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.presentation.component.ChatItemHeaderRow
import com.asimorphic.chat.presentation.model.ChatUi
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
fun ChatListItemUi(
    chat: ChatUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val isGroupChat = chat.otherParticipants.size > 1

    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .background(
                color = if (isSelected)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.extended.surfaceLower
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(all = 16.dp),
            verticalArrangement = Arrangement
                .spacedBy(space = 12.dp)
        ) {
            ChatItemHeaderRow(
                chatUi = chat,
                isGroupChat = isGroupChat,
                modifier = Modifier.fillMaxWidth()
            )

            if (chat.lastMessage != null) {
                val previewMessage = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.extended.textSecondary
                        )
                    ) {
                        append(chat.lastMessageSender + ": ")
                    }
                    append(chat.lastMessage.content)
                }
                Text(
                    text = previewMessage,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box(
            modifier = Modifier
                .alpha(alpha = if (isSelected) 1f else 0f)
                .background(color = MaterialTheme.colorScheme.primary)
                .width(width = 4.dp)
        )
    }
}

@Composable
@Preview
fun ChatListItemUiPreview() {
    ChirpTheme(darkMode = true) {
        ChatListItemUi(
            isSelected = true,
            modifier = Modifier.fillMaxWidth(),
            chat = ChatUi(
                id = "0",
                selfParticipant = ChatParticipantUi(
                    id = "0",
                    username = "Asim",
                    initials = "AA"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = "1",
                        username = "John",
                        initials = "JD"
                    ),
                    ChatParticipantUi(
                        id = "2",
                        username = "Thomas",
                        initials = "TH"
                    ),
                ),
                lastMessage = ChatMessage(
                    id = "0",
                    chatId = "0",
                    content = "Hello world! This message showcases the last message preview " +
                            "from the Chirp application. It is too lengthy to show entirely in" +
                            "preview so will be ellipsised",
                    createdAt = Clock.System.now(),
                    senderId = "0"
                ),
                lastMessageSender = "Asim",
            )
        )
    }
}