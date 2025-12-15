package com.asimorphic.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ChirpTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkMode) DarkColorScheme else LightColorScheme
    val extendedScheme = if (darkMode) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        value = LocalExtendedColors provides extendedScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}