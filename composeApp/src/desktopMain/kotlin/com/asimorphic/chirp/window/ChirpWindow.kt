package com.asimorphic.chirp.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import chirp.composeapp.generated.resources.Res
import chirp.composeapp.generated.resources.chirp_logo_w_bg
import chirp.composeapp.generated.resources.file
import chirp.composeapp.generated.resources.new_window
import com.asimorphic.chirp.App
import com.asimorphic.chirp.theme.ApplicationTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChirpWindow(
    onAddWindowClick: () -> Unit,
    onCloseRequest: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    applicationTheme: ApplicationTheme
) {
    val windowState = rememberWindowState(
        width = 1250.dp,
        height = 750.dp
    )

    Window(
        title = "Chirp",
        state = windowState,
        onCloseRequest = onCloseRequest,
        icon = painterResource(Res.drawable.chirp_logo_w_bg)
    ) {
        MenuBar {
            Menu(
                text = stringResource(Res.string.file),
                mnemonic = 'F'
            ) {
                Item(
                    text = stringResource(Res.string.new_window),
                    mnemonic = 'N',
                    shortcut = KeyShortcut(
                        meta = true,
                        key = Key.N,
                    ),
                    onClick = onAddWindowClick
                )
            }
        }

        App(isDarkMode = applicationTheme == ApplicationTheme.DARK)
    }
}