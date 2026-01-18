package com.asimorphic.core.designsystem.component.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpChatBubble(
    from: String,
    messageBody: String,
    formattedTimestamp: String,
    triangleAlign: TriangleAlign,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.extended.surfaceHigher,
    messageStatus: @Composable (() -> Unit)? = null,
    triangleSize: Dp = 12.dp,
    onLongClick: (() -> Unit)? = null
) {
    val padding  = 14.dp
    Column(
        modifier = modifier
            .then(
                other = if (onLongClick != null) {
                    Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            color = MaterialTheme.colorScheme.extended.surfaceOutline
                        ),
                        onLongClick = onLongClick,
                        onClick = {}
                    )
                } else Modifier
            ).clip(
                shape = ChatBubbleShape(
                    triangleAlign = triangleAlign,
                    triangleSize = triangleSize
                )
            ).background(
                color = color
            ).padding(
                start = if (triangleAlign == TriangleAlign.LEFT) {
                    padding + triangleSize
                } else padding,
                end = if (triangleAlign == TriangleAlign.RIGHT) {
                    padding + triangleSize
                } else padding,
                top = padding,
                bottom = padding
            ),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = from,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(weight = 1f)
            )
            Spacer(modifier = Modifier.width(width = 22.dp))
            Text(
                text = formattedTimestamp,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Text(
            text = messageBody,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        messageStatus?.invoke()
    }
}

@Composable
@Preview
fun ChirpChatBubbleLeftPreview() {
    ChirpTheme(darkMode = true) {
        ChirpChatBubble(
            from = "Asim",
            messageBody = "This is a very crazy really incredibly long sentence to test the Chirp chat bubble user experience, also how has your day been? Weather's nice today.",
            formattedTimestamp = "Saturday 6:34pm",
            triangleAlign = TriangleAlign.LEFT,
            modifier = Modifier.widthIn(max = 250.dp),
            color = MaterialTheme.colorScheme.extended.accentGreen
        )
    }
}

@Composable
@Preview
fun ChirpChatBubbleRightPreview() {
    ChirpTheme {
        ChirpChatBubble(
            from = "Asim",
            messageBody = "Hello world!",
            formattedTimestamp = "Saturday 6:34pm",
            triangleAlign = TriangleAlign.RIGHT,
            modifier = Modifier.widthIn(max = 250.dp)
        )
    }
}

