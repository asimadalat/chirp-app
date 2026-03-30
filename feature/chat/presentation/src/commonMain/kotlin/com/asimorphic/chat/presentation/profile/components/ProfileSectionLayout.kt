package com.asimorphic.chat.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.presentation.util.DeviceScreenSizeType
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType

@Composable
fun ProfileSectionLayout(
    headerText: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val deviceScreenSizeType = currentDeviceScreenSizeType()
    when (deviceScreenSizeType) {
        DeviceScreenSizeType.MOBILE_PORTRAIT -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = headerText,
                    color = MaterialTheme.colorScheme.extended.textTertiary,
                    style = MaterialTheme.typography.labelSmall
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    content()
                }
            }
        }
        else -> {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = headerText,
                    color = MaterialTheme.colorScheme.extended.textTertiary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(0.25f)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(0.75f)
                ) {
                    content()
                }
            }
        }
    }
}