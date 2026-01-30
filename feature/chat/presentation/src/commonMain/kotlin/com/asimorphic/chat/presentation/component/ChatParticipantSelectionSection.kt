package com.asimorphic.chat.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.component.profile_picture.ProfilePictureUi
import com.asimorphic.core.presentation.util.DeviceScreenSizeType
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType

@Composable
fun ColumnScope.ChatParticipantSelectionSection(
    selectedParticipants: List<ProfilePictureUi>,
    modifier: Modifier = Modifier,
    searchResult: ProfilePictureUi? = null
) {
    val deviceScreenSizeType = currentDeviceScreenSizeType()
    val rootHeightModifier = when (deviceScreenSizeType) {
        DeviceScreenSizeType.TABLET_PORTRAIT,
        DeviceScreenSizeType.TABLET_LANDSCAPE,
        DeviceScreenSizeType.DESKTOP -> {
            Modifier.animateContentSize()
                .heightIn(min = 200.dp, max = 300.dp)
        }
        else -> Modifier.weight(weight = 1f)
    }

    Box(
        modifier = rootHeightModifier
            .then(other = modifier)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            searchResult?.let {
                item {
                    ChatParticipantListItem(
                        participantUi = searchResult,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            if (selectedParticipants.isNotEmpty() && searchResult == null) {
                items(
                    items = selectedParticipants,
                    key = { it.id }
                ) { participant ->
                    ChatParticipantListItem(
                        participantUi = participant,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}