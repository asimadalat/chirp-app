package com.asimorphic.chat.presentation.chat_menu.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.logout
import chirp.feature.chat.presentation.generated.resources.manage_profile
import com.asimorphic.core.designsystem.component.dropdown.ChirpDropdownMenu
import com.asimorphic.core.designsystem.component.dropdown.DropdownItem
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.designsystem.component.profile_picture.ChirpProfilePicture
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatListProfilePictureHeaderSection(
    selfParticipant: ChatParticipantUi?,
    isMenuOpen: Boolean,
    onDismissMenu: () -> Unit,
    onClick: () -> Unit,
    onManageProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        selfParticipant?.let {
            ChirpProfilePicture(
                displayText = selfParticipant.initials,
                imageUrl = selfParticipant.imageUrl,
                onClick = onClick
            )
        }

        ChirpDropdownMenu(
            isOpen = isMenuOpen,
            onDismiss = onDismissMenu,
            items = listOf(
                DropdownItem(
                    label = stringResource(resource = Res.string.manage_profile),
                    icon = Icons.Rounded.Person,
                    contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                    onClick = onManageProfileClick
                ),
                DropdownItem(
                    label = stringResource(resource = Res.string.logout),
                    icon = Icons.AutoMirrored.Rounded.ExitToApp,
                    contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                    onClick = onLogoutClick
                )
            )
        )
    }
}