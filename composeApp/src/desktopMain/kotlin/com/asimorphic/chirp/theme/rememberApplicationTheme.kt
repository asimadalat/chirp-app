package com.asimorphic.chirp.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.asimorphic.core.domain.preferences.ThemePreference
import com.jthemedetecor.OsThemeDetector
import java.util.function.Consumer

@Composable
fun rememberApplicationTheme(
    setThemePreference: ThemePreference
): ApplicationTheme {
    var isSystemThemeDark by remember {
        if (OsThemeDetector.isSupported()) {
            mutableStateOf(OsThemeDetector.getDetector().isDark)
        } else {
            val isSetPreferenceDark = setThemePreference == ThemePreference.DARK
            mutableStateOf(isSetPreferenceDark)
        }
    }

    DisposableEffect(Unit) {
        var listener: Consumer<Boolean>? = null
        if (OsThemeDetector.isSupported()) {
            listener = Consumer<Boolean> { dark -> isSystemThemeDark = dark }
            OsThemeDetector.getDetector().registerListener(listener)
        }
        onDispose {
            OsThemeDetector.getDetector().removeListener(listener)
        }
    }

    val isDarkTheme = when (setThemePreference) {
        ThemePreference.SYSTEM -> isSystemThemeDark
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }

    return if (isDarkTheme) ApplicationTheme.DARK else ApplicationTheme.LIGHT
}