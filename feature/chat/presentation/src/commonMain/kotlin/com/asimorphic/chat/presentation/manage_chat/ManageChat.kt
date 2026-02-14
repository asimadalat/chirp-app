package com.asimorphic.chat.presentation.manage_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.people
import chirp.feature.chat.presentation.generated.resources.save
import com.asimorphic.chat.presentation.component.manage_chat.ManageChatAction
import com.asimorphic.chat.presentation.component.manage_chat.ManageChatScreen
import com.asimorphic.core.designsystem.component.layout.ChirpDialogSheetLayout
import com.asimorphic.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManageChatRoot(
    chatId: String?,
    onParticipantsAdded: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: ManageChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = chatId) {
        viewModel.onAction(
            action = ManageChatAction.ChatParticipants.OnSelectChat(
                chatId = chatId
            )
        )
    }

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ManageChatEvent.OnParticipantsAdded -> onParticipantsAdded()
        }
    }

    ChirpDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ManageChatScreen(
            headerText = stringResource(resource = Res.string.people),
            primaryButtonText = stringResource(resource = Res.string.save),
            state = state,
            onAction = { action ->
                when (action) {
                    ManageChatAction.OnDismissDialog -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action = action)
            }
        )
    }
}