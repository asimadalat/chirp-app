@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)

package com.asimorphic.chat.presentation.chat_menu_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.create_chat
import com.asimorphic.chat.presentation.create_chat.CreateChatRoot
import com.asimorphic.core.designsystem.component.button.ChirpFloatingActionButton
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.presentation.util.DialogSheetScopedViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatMenuDetailLayout(
    chatListDetailViewModel: ChatListDetailViewModel = koinViewModel()
) {
    val sharedState by chatListDetailViewModel.state.collectAsStateWithLifecycle()
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective
    )
    val scope = rememberCoroutineScope()

    BackHandler(enabled = scaffoldNavigator.canNavigateBack()) {
        scope.launch {
            scaffoldNavigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        directive = scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.extended.surfaceLower
        ),
        listPane = {
            AnimatedPane {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        ChirpFloatingActionButton(
                            onClick =  {
                                chatListDetailViewModel.onAction(action = ChatListDetailAction.OnCreateChatClick)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = stringResource(resource = Res.string.create_chat)
                            )
                        }
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingValues
                    ) {
                        items(count = 100) { chatIndex ->
                            Text(
                                text = "$chatIndex",
                                modifier = Modifier.clickable {
                                    chatListDetailViewModel.onAction(
                                        action = ChatListDetailAction.OnChatClick(chatId = chatIndex.toString())
                                    )
                                    scope.launch {
                                        scaffoldNavigator.navigateTo(
                                            pane = ListDetailPaneScaffoldRole.Detail
                                        )
                                    }
                                }.padding(all = 18.dp)
                            )
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    sharedState.selectedChatId?.let {
                        Text(
                            text = it
                        )
                    }
                }
            }
        }
    )

    DialogSheetScopedViewModel(
        isVisible = sharedState.dialogState is ChatMenuDetailDialogState.CreateChat
    ) {
        CreateChatRoot(
            onDismiss = {
                chatListDetailViewModel.onAction(
                    action = ChatListDetailAction.OnDismissCurrentDialog
                )
            },
            onChatCreated = { chat ->
                chatListDetailViewModel.onAction(
                    action = ChatListDetailAction.OnDismissCurrentDialog
                )
                chatListDetailViewModel.onAction(
                    action = ChatListDetailAction.OnChatClick(chatId = chat.id)
                )
                scope.launch {
                    scaffoldNavigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                }
            }
        )
    }
}