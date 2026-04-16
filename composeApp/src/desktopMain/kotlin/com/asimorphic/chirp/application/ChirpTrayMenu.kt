package com.asimorphic.chirp.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.TrayState
import chirp.composeapp.generated.resources.Res
import chirp.composeapp.generated.resources.app_theme
import chirp.composeapp.generated.resources.chirp_logo_w_bg
import com.asimorphic.core.domain.preferences.ThemePreference
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationScope.ChirpTrayMenu(
    state: TrayState,
    setThemePreference: ThemePreference,
    onThemePreferenceClick: (ThemePreference) -> Unit
) {
    Tray(
        icon = painterResource(Res.drawable.chirp_logo_w_bg),
        state = state,
        tooltip = "Chirp"
    ) {
        Menu(
            text = stringResource(Res.string.app_theme)
        ) {
            ThemePreference.entries.forEach { themePreference ->
                CheckboxItem(
                    text = themePreference.name.lowercase().replaceFirstChar { it.titlecase() },
                    onCheckedChange = { onThemePreferenceClick(themePreference) },
                    checked = setThemePreference == themePreference
                )
            }
        }
    }
}