package com.asimorphic.chat.presentation.chat_menu.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.logout
import chirp.feature.chat.presentation.generated.resources.manage_profile
import com.asimorphic.core.designsystem.component.brand.ChirpHorizontalDivider
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
    onProfileSettingsClick: () -> Unit,
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

        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = onDismissMenu,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(size = 16.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline
            )
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = stringResource(resource = Res.string.manage_profile),
                            tint = MaterialTheme.colorScheme.extended.textSecondary,
                            modifier = Modifier.size(size = 20.dp)
                        )
                        Text(
                            text = stringResource(resource = Res.string.manage_profile),
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onProfileSettingsClick()
                }
            )
            ChirpHorizontalDivider()
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                            contentDescription = stringResource(resource = Res.string.logout),
                            tint = MaterialTheme.colorScheme.extended.destructiveHover,
                            modifier = Modifier.size(size = 20.dp)
                        )
                        Text(
                            text = stringResource(resource = Res.string.logout),
                            color = MaterialTheme.colorScheme.extended.destructiveHover,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onLogoutClick()
                }
            )
        }
    }
}