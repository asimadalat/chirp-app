package com.asimorphic.core.designsystem.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.component.brand.ChirpLogoBrand
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import com.asimorphic.core.presentation.util.DeviceScreenSizeType
import com.asimorphic.core.presentation.util.currentDeviceScreenSizeType
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpFormLayout(
    headerText: String,
    logo: @Composable () -> Unit,
    formContent: @Composable ColumnScope.() -> Unit,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    val screenSizeType: DeviceScreenSizeType = currentDeviceScreenSizeType()
    val headerColor = if (screenSizeType == DeviceScreenSizeType.MOBILE_LANDSCAPE) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.extended.textPrimary
    }

    when (screenSizeType) {
        DeviceScreenSizeType.MOBILE_PORTRAIT -> {
            ChirpSurface(
                modifier = Modifier
                    .consumeWindowInsets(insets = WindowInsets.navigationBars)
                    .consumeWindowInsets(insets = WindowInsets.displayCutout),
                header =  {
                    Spacer(
                        modifier = Modifier
                            .height(height = 30.dp)
                    )
                    logo()
                    Spacer(
                        modifier = Modifier
                            .height(height = 30.dp)
                    )
                }
            ) {
                Spacer(
                    modifier = Modifier
                        .height(height = 24.dp)
                )
                ChirpAuthHeaderSection(
                    headerText = headerText,
                    headerColor = headerColor,
                    errorText = errorText
                )
                Spacer(
                    modifier = Modifier
                        .height(height = 24.dp)
                )
                formContent()
            }
        }
        DeviceScreenSizeType.MOBILE_LANDSCAPE -> {
            Row(
                horizontalArrangement = Arrangement
                    .spacedBy(
                        space = 16.dp
                    ),
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(insets = WindowInsets.displayCutout)
            ) {
                Column(
                    modifier = Modifier
                        .weight(weight = 1f),
                    verticalArrangement = Arrangement
                        .spacedBy(
                            space = 24.dp
                        )
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(height = 16.dp)
                    )
                    logo()
                    ChirpAuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText
                    )
                }
                ChirpSurface(
                      modifier = Modifier
                          .weight(weight = 1f)
                ) {
                    formContent()
                }
            }
        }
        DeviceScreenSizeType.TABLET_PORTRAIT,
        DeviceScreenSizeType.TABLET_LANDSCAPE,
        DeviceScreenSizeType.DESKTOP -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
                    .padding(
                        top = 32.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement
                    .spacedBy(
                        space = 32.dp
                    )
            ) {
                logo()
                Column(
                    modifier = Modifier
                        .widthIn(
                            max = 480.dp
                        )
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(size = 32.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                        .padding(
                            horizontal = 26.dp,
                            vertical = 34.dp
                        ),
                    verticalArrangement = Arrangement
                        .spacedBy(
                            space = 24.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ChirpAuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText
                    )
                    formContent()
                }
            }
        }
    }
}

@Composable
@Preview
fun ChirpFormLayoutLightPreview() {
    ChirpTheme {
        ChirpFormLayout(
            headerText = "Register for Chirp!",
            errorText = "Username or password is incorrect.",
            logo = { ChirpLogoBrand() },
            formContent = {
                Text(
                    text = "Register",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}

@Composable
@Preview
fun ChirpFormLayoutDarkPreview() {
    ChirpTheme(darkMode = true) {
        ChirpFormLayout(
            headerText = "Register for Chirp!",
            errorText = "Username or password is incorrect.",
            logo = { ChirpLogoBrand() },
            formContent = {
                Text(
                    text = "Register",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}