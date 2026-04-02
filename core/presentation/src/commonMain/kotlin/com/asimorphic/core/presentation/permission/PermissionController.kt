package com.asimorphic.core.presentation.permission

expect class PermissionController {
    suspend fun requestPermission(
        permission: Permission
    ): PermissionState
}