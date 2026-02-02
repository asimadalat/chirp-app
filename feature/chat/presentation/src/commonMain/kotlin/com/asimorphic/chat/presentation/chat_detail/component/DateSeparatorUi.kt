package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.component.brand.ChirpHorizontalDivider
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DateSeparatorUi(
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment
            .CenterVertically
    ) {
        ChirpHorizontalDivider(
            modifier = Modifier.weight(weight = 1f)
        )
        Text(
            text = date,
            color = MaterialTheme.colorScheme.extended.textPlaceholder,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        ChirpHorizontalDivider(
            modifier = Modifier.weight(weight = 1f)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DateSeparatorUiPreview() {
    ChirpTheme {
        DateSeparatorUi(date = "01/02/2026")
    }
}