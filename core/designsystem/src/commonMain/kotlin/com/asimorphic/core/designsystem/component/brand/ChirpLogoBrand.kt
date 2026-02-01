package com.asimorphic.core.designsystem.component.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.chirp_logo
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChirpLogoBrand(
    tint: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(resource = Res.drawable.chirp_logo),
        contentDescription = null,
        tint = tint,
        modifier = modifier
    )
}