package com.asimorphic.chat.presentation.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.asimorphic.core.designsystem.theme.extended

@Composable
fun getChatBubbleColourForUser(userId: String): Color {
    val availableColours = with(
        receiver = MaterialTheme.colorScheme.extended
    ) {
        listOf(
            cakeTeal,
            cakeRed,
            cakeBlue,
            cakeMint,
            cakePink,
            cakeGreen,
            cakeOrange,
            cakePurple,
            cakeViolet,
            cakeYellow
        )
    }

    val index = userId.hashCode().toUInt() % availableColours.size.toUInt()
    return availableColours[index.toInt()]
}