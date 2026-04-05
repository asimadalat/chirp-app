package com.asimorphic.chirp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asimorphic.chirp.navigation.ExternalUriHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var showSplashScreen = true

        installSplashScreen().setKeepOnScreenCondition {
            showSplashScreen
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleChatMessageDeeplink(intent)

        setContent {
            App(
                onAuthStatusChecked = {
                    showSplashScreen = false
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleChatMessageDeeplink(intent)
    }

    private fun handleChatMessageDeeplink(intent: Intent) {
        val chatId = intent.getStringExtra("chatId")
            ?: intent.extras?.getString("chatId")

        chatId?.let {
            ExternalUriHandler.onNewUri("chirp://chat_detail/$chatId")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}