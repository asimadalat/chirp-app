package com.asimorphic.chat.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        val databaseFile = context
            .applicationContext
            .getDatabasePath(ChirpChatDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = context.applicationContext,
            name = databaseFile.absolutePath
        )
    }
}