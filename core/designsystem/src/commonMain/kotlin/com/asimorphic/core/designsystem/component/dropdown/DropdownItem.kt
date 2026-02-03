package com.asimorphic.core.designsystem.component.dropdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DropdownItem(
    val label: String,
    val icon: ImageVector,
    val contentColor: Color,
    val onClick: () -> Unit
)
