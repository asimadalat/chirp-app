@file:OptIn(ExperimentalForeignApi::class)

package com.asimorphic.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        val databaseFile = documentDirectory() + "/${ChirpChatDatabase.DB_NAME}"

        return Room
            .databaseBuilder(
                name = databaseFile
            )
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager
            .defaultManager
            .URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                create = false,
                appropriateForURL = null,
                error = null
            )

        return requireNotNull(
            value = documentDirectory?.path
        )
    }
}