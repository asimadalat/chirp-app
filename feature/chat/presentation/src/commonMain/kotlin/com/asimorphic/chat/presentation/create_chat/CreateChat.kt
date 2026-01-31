package com.asimorphic.chat.presentation.create_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.cancel
import chirp.feature.chat.presentation.generated.resources.create_chat
import com.asimorphic.chat.domain.model.Chat
import com.asimorphic.chat.presentation.component.ChatParticipantSearchSection
import com.asimorphic.chat.presentation.component.ChatParticipantSelectionSection
import com.asimorphic.chat.presentation.component.ManageChatActionSection
import com.asimorphic.chat.presentation.component.ManageChatHeaderRow
import com.asimorphic.core.designsystem.component.brand.ChirpHorizontalDivider
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.component.layout.ChirpDialogSheetLayout
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.presentation.util.DeviceScreenSizeType
import com.asimorphic.core.presentation.util.ObserveAsEvents
import com.asimorphic.core.presentation.util.clearFocusOnTap
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateChatRoot(
    onDismiss: () -> Unit,
    onChatCreated: (Chat) -> Unit,
    viewModel: CreateChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is CreateChatEvent.OnChatCreated -> onChatCreated(event.chat)
        }
    }

    ChirpDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        CreateChatScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    CreateChatAction.OnDismissDialog -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action = action)
            }
        )
    }
}

@Composable
fun CreateChatScreen(
    state: CreateChatState,
    onAction: (CreateChatAction) -> Unit,
) {
    var isTextFieldFocused by remember { mutableStateOf(value = false) }
    val imeHeight = WindowInsets.ime.getBottom(density = LocalDensity.current)
    val isKeyboardVisible = imeHeight > 0
    val screenSizeType = currentDeviceScreenSizeType()

    val shouldHideHeader = screenSizeType == DeviceScreenSizeType.MOBILE_LANDSCAPE
            || (isKeyboardVisible && screenSizeType != DeviceScreenSizeType.DESKTOP)
            || isTextFieldFocused

    Column(
        modifier = Modifier.clearFocusOnTap()
            .fillMaxWidth()
            .wrapContentHeight()
            .imePadding()
            .background(color = MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = !shouldHideHeader
        ) {
            Column {
                ManageChatHeaderRow(
                    title = stringResource(resource = Res.string.create_chat),
                    onCloseClick = {
                        onAction(CreateChatAction.OnDismissDialog)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                ChirpHorizontalDivider()
            }
        }
        ChatParticipantSearchSection(
            queryState = state.queryTextFieldState,
            isSearchEnabled = state.canAddParticipant,
            isLoading = state.isSearchingParticipants,
            onAddClick = {
                onAction(CreateChatAction.OnAddClick)
            },
            modifier = Modifier.fillMaxWidth(),
            error = state.searchError,
            onFocusChanged = {
                isTextFieldFocused = it
            }
        )
        ChirpHorizontalDivider()
        ChatParticipantSelectionSection(
            selectedParticipants = state.selectedChatParticipants,
            modifier = Modifier.fillMaxWidth(),
            searchResult = state.currentSearchResult
        )
        ChirpHorizontalDivider()
        ManageChatActionSection(
            primaryButton = {
                ChirpButton(
                    text = stringResource(resource = Res.string.create_chat),
                    onClick = {
                        onAction(CreateChatAction.OnCreateChatClick)
                    },
                    isLoading = state.isCreatingChat,
                    enabled = state.selectedChatParticipants.isNotEmpty()
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = stringResource(resource = Res.string.cancel),
                    onClick = {
                        onAction(CreateChatAction.OnDismissDialog)
                    },
                    type = ChirpButtonType.SECONDARY
                )
            },
            error = state.createChatError?.asString(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        CreateChatScreen(
            state = CreateChatState(),
            onAction = {}
        )
    }
}