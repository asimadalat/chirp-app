package com.asimorphic.chat.presentation.chat_detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.asimorphic.chat.presentation.model.MessageUi
import com.asimorphic.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatDetailListItemUi(
    messageUi: MessageUi,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when (messageUi) {
            is MessageUi.DateSeparator -> {
                DateSeparatorUi(
                    date = messageUi.date.asString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is MessageUi.OtherParticipantMessage -> {
                OtherParticipantMessageUi(
                    message = messageUi
                )
            }
            is MessageUi.SelfParticipantMessage -> {
                SelfParticipantMessageUi(
                    message = messageUi,
                )
            }
        }
    }
}

@Composable
@Preview
fun ChatDetailListItemUi() {
    ChirpTheme {
        ChatDetailListItemUi()
    }
}