package com.asimorphic.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.extended

object ChirpButtonDefaults {

    @Composable
    fun buttonColors(type: ChirpButtonType): ButtonColors {
        val (container, content) = resolveBaseColors(type)

        return ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    }

    @Composable
    fun iconButtonColors(type: ChirpButtonType): IconButtonColors {
        val (container, content) = resolveBaseColors(type)

        return IconButtonDefaults.iconButtonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    }

    @Composable
    fun border(
        type: ChirpButtonType,
        enabled: Boolean
    ): BorderStroke? {

        val defaultBorder = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.extended.disabledOutline
        )

        return when {
            type == ChirpButtonType.PRIMARY && !enabled -> defaultBorder
            type == ChirpButtonType.SECONDARY -> defaultBorder
            type == ChirpButtonType.DESTRUCTIVE_PRIMARY && !enabled -> defaultBorder
            type == ChirpButtonType.DESTRUCTIVE_SECONDARY -> {
                val borderColour = if (enabled) {
                    MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
                } else {
                    MaterialTheme.colorScheme.extended.disabledOutline
                }
                BorderStroke(1.dp, borderColour)
            }
            else -> null
        }
    }

    @Composable
    private fun resolveBaseColors(
        type: ChirpButtonType
    ): Pair<Color, Color> {
        return when (type) {
            ChirpButtonType.PRIMARY ->
                MaterialTheme.colorScheme.primary to
                        MaterialTheme.colorScheme.onPrimary

            ChirpButtonType.SECONDARY ->
                Color.Transparent to
                        MaterialTheme.colorScheme.extended.textSecondary

            ChirpButtonType.DESTRUCTIVE_PRIMARY ->
                MaterialTheme.colorScheme.error to
                        MaterialTheme.colorScheme.onError

            ChirpButtonType.DESTRUCTIVE_SECONDARY ->
                Color.Transparent to
                        MaterialTheme.colorScheme.error

            ChirpButtonType.TEXT ->
                Color.Transparent to
                        MaterialTheme.colorScheme.tertiary
        }
    }
}
