package com.asimorphic.chat.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.drop_picture_prompt
import chirp.feature.chat.presentation.generated.resources.upload
import chirp.feature.chat.presentation.generated.resources.upload_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DragAndDropOverlay(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 18.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier.fillMaxSize().background(
            Color.Black.copy(alpha = 0.8f)
        )
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.upload_icon),
            contentDescription = stringResource(Res.string.upload),
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = stringResource(Res.string.drop_picture_prompt),
            style = MaterialTheme.typography.titleMedium
        )
    }
}