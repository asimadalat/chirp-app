package com.asimorphic.chat.presentation.chat_menu_detail

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asimorphic.core.presentation.util.DeviceScreenSizeType
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType

@Composable
fun createNoSpacingPaneScaffoldDirective(): PaneScaffoldDirective {
    val screenSizeType = currentDeviceScreenSizeType()
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    val maxHorizontalPartitions = when (screenSizeType) {
        DeviceScreenSizeType.MOBILE_PORTRAIT,
        DeviceScreenSizeType.MOBILE_LANDSCAPE,
        DeviceScreenSizeType.TABLET_PORTRAIT -> 1
        DeviceScreenSizeType.TABLET_LANDSCAPE,
        DeviceScreenSizeType.DESKTOP -> 2
    }

    val verticalPartitionSpacerSize: Dp
    val maxVerticalPartitions: Int

    if (windowAdaptiveInfo.windowPosture.isTabletop) {
        verticalPartitionSpacerSize = 24.dp
        maxVerticalPartitions = 2
    } else {
        verticalPartitionSpacerSize = 0.dp
        maxVerticalPartitions = 1
    }

    return PaneScaffoldDirective(
        maxVerticalPartitions = maxVerticalPartitions,
        verticalPartitionSpacerSize = verticalPartitionSpacerSize,
        maxHorizontalPartitions = maxHorizontalPartitions,
        horizontalPartitionSpacerSize = 0.dp,
        defaultPanePreferredWidth = 360.dp,
        excludedBounds = emptyList()
    )
}