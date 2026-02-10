package com.asimorphic.chirp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asimorphic.chat.database.ChirpChatDatabase
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    val db by inject<ChirpChatDatabase>()
    override fun onCreate(savedInstanceState: Bundle?) {
        var showSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition {
            showSplashScreen
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        println(db.toString())
        setContent {
            App(
                onAuthStatusChecked = {
                    showSplashScreen = false
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}