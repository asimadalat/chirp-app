package com.asimorphic.core.designsystem.component.brand

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChirpFailureIconBrand(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Rounded.Close,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
}