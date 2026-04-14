package com.asimorphic.chat.presentation.profile.media_picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.draganddrop.DragAndDropTarget

@Composable
actual fun rememberDragAndDropTarget(
    onHover: (Boolean) -> Unit,
    onDrop: (PickedImageData) -> Unit
): DragAndDropTarget {
    return remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                return false
            }
        }
    }
}