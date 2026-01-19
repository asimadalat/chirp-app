package com.asimorphic.core.designsystem.component.profile_picture

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ProfilePictureSize(val dp: Dp) {
    SMALL(dp = 50.dp),
    LARGE(dp = 80.dp)
}

@Composable
fun ChirpProfilePicture(
    modifier: Modifier = Modifier,
    displayText: String,
    size: ProfilePictureSize = ProfilePictureSize.SMALL,
    imageUrl: String? = null,
    onClick: (() -> Unit)? = null,
    textColour: Color = MaterialTheme.colorScheme.extended.textPlaceholder
) {
    Box(
        modifier = modifier.size(size = size.dp)
            .clip(shape = CircleShape)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .background(color = MaterialTheme.colorScheme.extended.secondaryFill)
            .border(
                width = 2.dp,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.outline
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText.uppercase(),
            color = textColour,
            style = MaterialTheme.typography.titleMedium
        )
        AsyncImage(
            model = imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.clip(CircleShape)
                .matchParentSize()
        )
    }
}

@Composable
@Preview
fun ChirpProfilePicturePreview() {
    ChirpTheme {
        ChirpProfilePicture(
            displayText = "AM",
            size = ProfilePictureSize.SMALL
        )
    }
}