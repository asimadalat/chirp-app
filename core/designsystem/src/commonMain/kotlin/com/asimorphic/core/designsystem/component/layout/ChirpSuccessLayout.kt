package com.asimorphic.core.designsystem.component.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.asimorphic.core.designsystem.component.brand.ChirpSuccessIconBrand
import com.asimorphic.core.designsystem.component.button.ChirpButton
import com.asimorphic.core.designsystem.component.button.ChirpButtonType
import com.asimorphic.core.designsystem.theme.ChirpTheme
import com.asimorphic.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpSuccessLayout(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -(25).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color= MaterialTheme.colorScheme.extended.textPrimary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .height(height = 8.dp)
            )
            Text(
                text = description,
                color= MaterialTheme.colorScheme.extended.textSecondary,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .height(height = 24.dp)
            )
            primaryButton()

            if (secondaryButton != null) {
                Spacer(
                    modifier = Modifier
                        .height(height = 8.dp)
                )
                secondaryButton()
            }

            Spacer(
                modifier = Modifier
                    .height(height = 8.dp)
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpSuccessLayoutPreview() {
    ChirpTheme {
        ChirpSuccessLayout(
            title = "Verification Success!",
            description = "You have successfully verified your Chirp account.",
            icon = {
                ChirpSuccessIconBrand()
            },
            primaryButton = {
                ChirpButton(
                    text = "Start a chat",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Change email",
                    onClick = {},
                    type = ChirpButtonType.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        )
    }
}