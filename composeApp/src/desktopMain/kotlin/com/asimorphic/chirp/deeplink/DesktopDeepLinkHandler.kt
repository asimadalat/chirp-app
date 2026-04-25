package com.asimorphic.chirp.deeplink

import com.asimorphic.chirp.navigation.ExternalUriHandler
import java.awt.Desktop
import javax.swing.SwingUtilities

object DesktopDeepLinkHandler {

    val supportedUrlPatterns = listOf(
        Regex("^chirp://.*"),
        Regex("^https?://chirp\\.asimorphic\\.com/.*"),
        Regex("^https?://dev\\.chirp\\.asimorphic\\.com/.*")
    )

    private var isInitialized = false

    fun setup() {
        if (isInitialized || !Desktop.isDesktopSupported())
            return

        try {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.APP_OPEN_URI)) {
                desktop.setOpenURIHandler { event ->
                    val uri = event.uri.toString()
                    SwingUtilities.invokeLater {
                        processUri(uri)
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun processUri(uri: String) {
        if (!isValidUri(uri)) return

        val cleanUri = uri.trim('"', ' ')
        ExternalUriHandler.onNewUri(cleanUri)
    }

    private fun isValidUri(uri: String): Boolean =
        supportedUrlPatterns.any { it.matches(uri) }
}
