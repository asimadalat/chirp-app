package com.asimorphic.chat.presentation.profile.media_picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.select_a_profile_picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Files
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

@Composable
actual fun rememberImagePickerLauncher(onResult: (PickedImageData) -> Unit): ImagePickerLauncher {
    val scope = rememberCoroutineScope()
    val fileDialogTitle = stringResource(Res.string.select_a_profile_picture)
    return remember {
        ImagePickerLauncher(
            onLaunch = {
                scope.launch {
                    pickImage(fileDialogTitle)?.let { data ->
                        onResult(data)
                    }
                }
            }
        )
    }
}

private val allowedFileExtensions = listOf(
    "jpg",
    "jpeg",
    "png",
    "webp"
)

private fun getMimeTypeFromFilename(filename: String): String? {
    val fileExtension = filename.substringAfterLast(".", "").lowercase()
    return when (fileExtension) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "webp" -> "image/webp"
        else -> null
    }
}

private suspend fun pickImage(fileDialogTitle: String): PickedImageData? {
    val file = suspendCancellableCoroutine<File?>
    { continuation ->
        var fileDialog: FileDialog? = null
        continuation.invokeOnCancellation {
            SwingUtilities.invokeLater {
                fileDialog?.dispose()
            }
        }

        SwingUtilities.invokeLater {
            try {
                fileDialog = FileDialog(Frame(), fileDialogTitle, FileDialog.LOAD)
                fileDialog.filenameFilter = FilenameFilter { _, name ->
                    allowedFileExtensions.any {
                        name.endsWith(it)
                    }
                }
                fileDialog.isVisible = true

                val file = File(fileDialog.directory, fileDialog.file)
                continuation.resume(value = file)
            } catch (_: Exception) {
                continuation.resume(value = null)
            }
        }
    }

    return withContext(Dispatchers.IO) {
        file ?: return@withContext null

        try {
            PickedImageData(
                bytes = Files.readAllBytes(file.toPath()),
                mimeType = getMimeTypeFromFilename(file.name)
            )
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            null
        }
    }
}
