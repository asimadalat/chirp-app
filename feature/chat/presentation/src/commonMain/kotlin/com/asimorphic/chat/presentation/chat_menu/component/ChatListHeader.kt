package com.asimorphic.chat.presentation.chat_menu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimorphic.chat.presentation.component.ChatHeader
import com.asimorphic.core.designsystem.component.brand.ChirpLogoBrand
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatListHeader(
    selfParticipant: ChatParticipantUi?,
    isMenuOpen: Boolean,
    onDismissMenu: () -> Unit,
    onUserProfilePictureClick: () -> Unit,
    onManageProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChatHeader(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ChirpLogoBrand(
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Chirp",
                color = MaterialTheme.colorScheme.extended.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            ChatListProfilePictureHeaderSection(
                selfParticipant = selfParticipant,
                isMenuOpen = isMenuOpen,
                onDismissMenu = onDismissMenu,
                onClick = onUserProfilePictureClick,
                onManageProfileClick = onManageProfileClick,
                onLogoutClick = onLogoutClick,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatListHeaderPreview() {
    ChirpTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatListHeader(
                selfParticipant = ChatParticipantUi(
                    id = "0",
                    username = "Asim",
                    initials = "AA"
                ),
                isMenuOpen = true,
                onDismissMenu = {},
                onUserProfilePictureClick = {},
                onManageProfileClick = {},
                onLogoutClick = {}
            )
        }
    }
}