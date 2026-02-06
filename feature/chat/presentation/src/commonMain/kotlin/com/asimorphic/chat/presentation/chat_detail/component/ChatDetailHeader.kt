package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.go_back
import chirp.feature.chat.presentation.generated.resources.leave_chat
import chirp.feature.chat.presentation.generated.resources.open_chat_options_menu
import chirp.feature.chat.presentation.generated.resources.people
import com.asimorphic.chat.domain.model.ChatMessage
import com.asimorphic.chat.presentation.component.ChatHeader
import com.asimorphic.chat.presentation.component.ChatItemHeaderRow
import com.asimorphic.chat.presentation.model.ChatUi
import com.asimorphic.core.designsystem.component.button.ChirpIconButton
import com.asimorphic.core.designsystem.component.dropdown.ChirpDropdownMenu
import com.asimorphic.core.designsystem.component.dropdown.DropdownItem
import com.asimorphic.core.designsystem.component.profile_picture.ChatParticipantUi
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
fun ChatDetailHeader(
    chatUi: ChatUi?,
    isChatListPresent: Boolean,
    isMenuDropdownOpen: Boolean,
    onChatOptionsClick: () -> Unit,
    onDismissChatOptions: () -> Unit,
    onManageChatClick: () -> Unit,
    onLeaveChatClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        horizontalArrangement = Arrangement
            .spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isChatListPresent) {
            ChirpIconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(resource = Res.string.go_back),
                    modifier = Modifier.size(size = 20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }

        if (chatUi != null) {
            val isGroupChat = chatUi.otherParticipants.size > 1

            ChatItemHeaderRow(
                chatUi = chatUi,
                isGroupChat = isGroupChat,
                modifier = Modifier
                    .weight(
                        weight = 1f
                    )
                    .clickable {
                        onManageChatClick()
                    }
            )
        } else {
            Spacer(modifier = Modifier.weight(weight = 1f))
        }

        Box {
            ChirpIconButton(
                onClick = onChatOptionsClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    tint = MaterialTheme.colorScheme.extended.textSecondary,
                    contentDescription = stringResource(
                        resource = Res.string.open_chat_options_menu
                    ),
                    modifier = Modifier
                        .size(
                            size = 20.dp
                        )
                )
            }

            ChirpDropdownMenu(
                isOpen = isMenuDropdownOpen,
                onDismiss = onDismissChatOptions,
                items = listOf(
                    DropdownItem(
                        icon = Icons.Rounded.Person,
                        label = stringResource(
                            resource = Res.string.people
                        ),
                        contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                        onClick = onManageChatClick
                    ),
                    DropdownItem(
                        icon = Icons.AutoMirrored.Rounded.ExitToApp,
                        label = stringResource(
                            resource = Res.string.leave_chat
                        ),
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onLeaveChatClick
                    )
                )
            )
        }
    }
}

@Composable
@Preview
fun ChatDetailHeaderPreview() {
    ChirpTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatHeader {
                ChatDetailHeader(
                    chatUi = ChatUi(
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
                    ),
                    isChatListPresent = false,
                    isMenuDropdownOpen = true,
                    onChatOptionsClick = {},
                    onDismissChatOptions = {},
                    onManageChatClick = {},
                    onLeaveChatClick = {},
                    onBackClick = {}
                )
            }
        }
    }
}