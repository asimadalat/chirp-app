package com.asimorphic.core.designsystem.component.brand

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChirpHorizontalDivider(
    modifier: Modifier = Modifier,
    startIndent: Dp = 0.dp
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth().padding(start = startIndent),
        color = MaterialTheme.colorScheme.outline
    )
}