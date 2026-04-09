package com.asimorphic.core.presentation.permission

actual class PermissionController {
    actual suspend fun requestPermission(permission: Permission): PermissionState = PermissionState.GRANTED
}