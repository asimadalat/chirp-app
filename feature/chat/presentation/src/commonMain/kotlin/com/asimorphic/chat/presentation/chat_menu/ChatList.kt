package com.asimorphic.chat.presentation.chat_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.cancel
import chirp.feature.chat.presentation.generated.resources.confirm_logout
import chirp.feature.chat.presentation.generated.resources.confirm_logout_desc
import chirp.feature.chat.presentation.generated.resources.create_chat
import chirp.feature.chat.presentation.generated.resources.logout
import chirp.feature.chat.presentation.generated.resources.no_chats
import chirp.feature.chat.presentation.generated.resources.no_chats_subtitle
import com.asimorphic.chat.presentation.chat_menu.component.ChatListHeader
import com.asimorphic.chat.presentation.chat_menu.component.ChatListItemUi
import com.asimorphic.chat.presentation.component.EmptyContentPlaceholder
import com.asimorphic.chat.presentation.model.ChatUi
import com.asimorphic.core.designsystem.component.brand.ChirpHorizontalDivider
import com.asimorphic.core.designsystem.component.button.ChirpFloatingActionButton
import com.asimorphic.core.designsystem.component.dialog.ChirpDestructiveConfirmationDialog
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListRoot(
    onChatClick: (ChatUi) -> Unit,
    onConfirmLogoutClick: () -> Unit,
    onCreateChatClick: () -> Unit,
    onManageProfileClick: () -> Unit,
    viewModel: ChatListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ChatListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ChatListAction.OnChatClick -> onChatClick(action.chat)
                ChatListAction.OnConfirmLogout -> onConfirmLogoutClick()
                ChatListAction.OnCreateChat -> onCreateChatClick()
                ChatListAction.OnManageProfileClick -> onManageProfileClick()
                else -> Unit
            }
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun ChatListScreen(
    state: ChatListState,
    onAction: (ChatListAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.extended.surfaceLower,
        floatingActionButton = {
            ChirpFloatingActionButton(
                onClick = { onAction(ChatListAction.OnCreateChat) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(resource = Res.string.create_chat)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ChatListHeader(
                selfParticipant = state.selfParticipant,
                isMenuOpen = state.isMenuOpen,
                onDismissMenu = {
                    onAction(ChatListAction.OnDismissMenu) },
                onUserProfilePictureClick = {
                    onAction(ChatListAction.OnUserProfilePictureClick) },
                onManageProfileClick = {
                    onAction(ChatListAction.OnManageProfileClick) },
                onLogoutClick = {
                    onAction(ChatListAction.OnLogoutClick) }
            )
            when {
                state.isLoadingChats -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                state.chats.isEmpty() -> {
                    EmptyContentPlaceholder(
                        title = stringResource(resource = Res.string.no_chats),
                        subtitle = stringResource(resource = Res.string.no_chats_subtitle),
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .weight(weight = 1f)
                    ) {
                        items(
                            items = state.chats,
                            key = { it.id }
                        ) { chat ->
                            ChatListItemUi(
                                chat = chat,
                                isSelected = chat.id == state.selectedChatId,
                                modifier = Modifier.fillMaxWidth()
                                    .clickable {
                                        onAction(ChatListAction.OnChatClick(chat = chat))
                                    }
                            )
                            ChirpHorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    if (state.showLogoutConfirmation) {
        ChirpDestructiveConfirmationDialog(
            title = stringResource(resource = Res.string.confirm_logout),
            description = stringResource(resource = Res.string.confirm_logout_desc),
            confirmButtonText = stringResource(resource = Res.string.logout),
            cancelButtonText = stringResource(resource = Res.string.cancel),
            onConfirmClick = {
                onAction(ChatListAction.OnConfirmLogout)
            },
            onCancelClick = {
                onAction(ChatListAction.OnDismissLogoutConfirmation)
            },
            onDismiss = {
                onAction(ChatListAction.OnDismissLogoutConfirmation)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        ChatListScreen(
            state = ChatListState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}