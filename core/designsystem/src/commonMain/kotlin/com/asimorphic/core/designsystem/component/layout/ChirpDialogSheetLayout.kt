package com.asimorphic.core.designsystem.component.layout

import androidx.compose.runtime.Composable
import com.asimorphic.core.designsystem.component.dialog.modal.ChirpBottomSheetModal
import com.asimorphic.core.designsystem.component.dialog.ChirpContentDialog
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType

@Composable
fun ChirpDialogSheetLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val screenSizeType = currentDeviceScreenSizeType()

    if (screenSizeType.isMobileView) {
        ChirpBottomSheetModal(
            onDismiss = onDismiss,
            content = content
        )
    }
    else {
        ChirpContentDialog(
            onDismiss = onDismiss,
            content = content
        )
    }
}