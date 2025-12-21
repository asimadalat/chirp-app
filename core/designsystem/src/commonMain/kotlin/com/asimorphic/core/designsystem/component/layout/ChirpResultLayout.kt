package com.asimorphic.core.designsystem.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.component.brand.ChirpLogoBrand
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.presentation.util.DeviceScreenSizeType
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpResultLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val screenSizeType = currentDeviceScreenSizeType()

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        if (screenSizeType == DeviceScreenSizeType.MOBILE_PORTRAIT) {
            ChirpSurface(
                modifier = Modifier
                    .padding(paddingValues = innerPadding),
                header = {
                    Spacer(
                        modifier = Modifier
                            .height(height = 34.dp)
                    )
                    ChirpLogoBrand()
                    Spacer(
                        modifier = Modifier
                            .height(height = 34.dp)
                    )
                },
                content = content
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(top = 32.dp),
                verticalArrangement = Arrangement
                    .spacedBy(space = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (screenSizeType != DeviceScreenSizeType.MOBILE_LANDSCAPE)
                    ChirpLogoBrand()

                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(size = 34.dp))
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(state = rememberScrollState()),
                    verticalArrangement = Arrangement
                        .spacedBy(space = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
@Preview
fun ChirpResultLayoutPreview() {
    ChirpTheme {
        ChirpResultLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            content = {
                Text(
                    text = "Account created successfully!",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        )
    }
}