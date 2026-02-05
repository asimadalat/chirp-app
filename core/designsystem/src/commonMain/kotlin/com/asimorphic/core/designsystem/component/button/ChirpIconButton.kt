package com.asimorphic.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    OutlinedIconButton(
        modifier = modifier
            .size(size = 45.dp),
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(size = 10.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary
        )
    ) {
        content()
    }
}

@Composable
@Preview
fun ChirpIconButtonPreview() {
    ChirpTheme {
        ChirpIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview
fun ChirpIconButtonDarkModePreview() {
    ChirpTheme(darkMode = true) {
        ChirpIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null
            )
        }
    }
}