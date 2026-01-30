package com.asimorphic.chat.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.component.profile_picture.ChirpProfilePicture
import com.asimorphic.core.designsystem.component.profile_picture.ProfilePictureUi
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.designsystem.theme.titleXSmall

@Composable
fun ChatParticipantListItem(
    participantUi: ProfilePictureUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(all = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        ChirpProfilePicture(
            displayText = participantUi.initials,
            imageUrl = participantUi.imageUrl
        )
        Text(
            text = participantUi.username,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            style = MaterialTheme.typography.titleXSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}