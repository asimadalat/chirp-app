package com.asimorphic.chat.presentation.profile.media_picker

import androidx.compose.runtime.remember

@androidx.compose.runtime.Composable
actual fun rememberImagePickerLauncher(onResult: (PickedImageData) -> Unit): ImagePickerLauncher {
    return remember {
        ImagePickerLauncher(
            onLaunch = { }
        )
    }
}