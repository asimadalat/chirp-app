package com.asimorphic.chat.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.empty_chat
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.presentation.util.DeviceScreenSizeType
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType
import org.jetbrains.compose.resources.painterResource

@Composable
fun EmptyContentPlaceholder(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val screenSizeType = currentDeviceScreenSizeType()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(resource = Res.drawable.empty_chat),
            contentDescription = title,
            modifier = Modifier.size(
                size = if (screenSizeType == DeviceScreenSizeType.MOBILE_LANDSCAPE)
                           120.dp
                       else
                           200.dp
            )
        )
        Spacer(modifier = Modifier.height(height = 4.dp))
        Text(
            text =  title,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.extended.textSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}