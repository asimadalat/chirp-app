package com.asimorphic.core.designsystem.component.profile_picture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.asimorphic.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpProfilePictureStack(
    modifier: Modifier = Modifier,
    profilePictures: List<ChatParticipantUi>,
    size: ProfilePictureSize = ProfilePictureSize.SMALL,
    maxVisibleCount: Int = 2,
    overlapPercentage: Float = 0.55f
) {
    val overlapOffset = -(size.dp * overlapPercentage)
    val visibleProfilePictures = profilePictures
        .take(n = maxVisibleCount)

    val remainingCount = (profilePictures.size - maxVisibleCount)
        .coerceAtLeast(minimumValue = 0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = overlapOffset
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleProfilePictures.forEach { profilePictureUi ->
            ChirpProfilePicture(
                displayText = profilePictureUi.initials,
                imageUrl = profilePictureUi.imageUrl,
                size = size,
            )
        }

        if (remainingCount > 0) {
            ChirpProfilePicture(
                displayText = "$remainingCount+",
                size = size,
                textColour = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview
fun ChirpProfilePictureStackPreview() {
    ChirpTheme {
        ChirpProfilePictureStack(
            profilePictures = listOf(
                ChatParticipantUi(
                    id = "0",
                    username = "Asim",
                    initials = "AS"
                ),
                ChatParticipantUi(
                    id = "1",
                    username = "John",
                    initials = "JD"
                ),ChatParticipantUi(
                    id = "2",
                    username = "Chris",
                    initials = "CW"
                ),ChatParticipantUi(
                    id = "3",
                    username = "Jack",
                    initials = "JS"
                ),ChatParticipantUi(
                    id = "4",
                    username = "Hello",
                    initials = "HW"
                ),
            )
        )
    }
}