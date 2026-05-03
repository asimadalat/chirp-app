package com.asimorphic.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.asimorphic.core.data.util.appDataDirectory
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        val directory = appDataDirectory

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val databaseFile = File(directory, ChirpChatDatabase.DB_NAME)

        return Room
            .databaseBuilder<ChirpChatDatabase>(
                name = databaseFile.absolutePath
            )
            .fallbackToDestructiveMigration(
                dropAllTables = true
            )
    }
}