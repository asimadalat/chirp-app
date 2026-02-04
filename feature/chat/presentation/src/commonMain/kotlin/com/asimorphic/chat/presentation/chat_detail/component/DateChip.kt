package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DateChip(
    date: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(percent = 100)
            )
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(percent = 100)
            )
    ) {
        Text(
            text = date,
            color = MaterialTheme.colorScheme.extended.textPlaceholder,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(
                vertical = 4.dp,
                horizontal = 12.dp
            )
        )
    }
}

@Composable
@Preview
fun DateChipPreview() {
    ChirpTheme {
        DateChip(
            date = "May 4"
        )
    }
}