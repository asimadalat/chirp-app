package com.asimorphic.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    type: ChirpButtonType = ChirpButtonType.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    startIcon: @Composable (() -> Unit)? = null
) {
    val colors = when (type) {
        ChirpButtonType.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonType.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonType.DESTRUCTIVE_PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonType.DESTRUCTIVE_SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonType.TEXT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    }

    val defaultBorder = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.extended.disabledOutline
    )
    val borders = when {
        type == ChirpButtonType.PRIMARY && !enabled -> defaultBorder
        type == ChirpButtonType.SECONDARY -> defaultBorder
        type == ChirpButtonType.DESTRUCTIVE_PRIMARY && !enabled -> defaultBorder
        type == ChirpButtonType.DESTRUCTIVE_SECONDARY -> {
            val borderColour = if (enabled) {
                MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
            } else {
                MaterialTheme.colorScheme.extended.disabledOutline
            }
            BorderStroke(
                width = 1.dp,
                color = borderColour
            )
        }
        else -> null
    }

    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(size = 10.dp),
        colors = colors,
        border = borders
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(size = 15.dp)
                    .alpha(
                        alpha = if (isLoading) 1f else 0f
                    ),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .alpha(
                        alpha = if (isLoading) 0f else 1f
                    )
            ) {
                startIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
@Preview
fun ChirpButtonPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello Chirp!",
            onClick = {}
        )
    }
}

@Composable
@Preview
fun ChirpSecondaryButtonPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello Chirp!",
            type = ChirpButtonType.SECONDARY,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun ChirpDestructiveButtonPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello Chirp!",
            type = ChirpButtonType.DESTRUCTIVE_PRIMARY,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun ChirpDestructiveSecondaryButtonPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello Chirp!",
            type = ChirpButtonType.DESTRUCTIVE_SECONDARY,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun ChirpTextButtonPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello Chirp!",
            type = ChirpButtonType.TEXT,
            onClick = {}
        )
    }
}

enum class ChirpButtonType {
    PRIMARY,
    SECONDARY,
    DESTRUCTIVE_PRIMARY,
    DESTRUCTIVE_SECONDARY,
    TEXT
}