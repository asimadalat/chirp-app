package com.asimorphic.core.presentation.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPermissionController(): PermissionController = remember { PermissionController() }