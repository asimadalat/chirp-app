package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.core.designsystem.component.chat.ChirpChatBubble
import com.asimorphic.core.designsystem.component.chat.TriangleAlign
import com.asimorphic.core.designsystem.component.profile_picture.ChirpProfilePicture

@Composable
fun OtherParticipantMessageUi(
    message: MessageUi.OtherParticipantMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement
            .spacedBy(space = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        ChirpProfilePicture(
            displayText = message.sender.initials,
            imageUrl = message.sender.imageUrl
        )
        ChirpChatBubble(
            messageBody = message.content,
            from = message.sender.username,
            formattedTimestamp = message.formattedSentTime.asString(),
            triangleAlign = TriangleAlign.LEFT,
        )
    }
}