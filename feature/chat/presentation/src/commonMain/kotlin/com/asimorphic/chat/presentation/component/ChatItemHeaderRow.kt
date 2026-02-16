package com.asimorphic.chat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.asimorphic.chat.presentation.model.ChatUi
import com.asimorphic.core.designsystem.component.profile_picture.ChirpProfilePictureStack
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.designsystem.theme.titleXSmall

@Composable
fun ChatItemHeaderRow(
    chatUi: ChatUi,
    isGroupChat: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(space = 12.dp)
    ) {
        ChirpProfilePictureStack(
            profilePictures = chatUi.otherParticipants
        )
        Column(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement
                .spacedBy(space = 6.dp)
        ) {
            Text(
                text = if (!isGroupChat)
                            chatUi.otherParticipants.first().username
                       else
                            chatUi.otherParticipants.take(n = 3)
                                .joinToString(separator = ", ")
                                { it.username },
                color = MaterialTheme.colorScheme.extended.textPrimary,
                style = MaterialTheme.typography.titleXSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}