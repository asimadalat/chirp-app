package com.asimorphic.chat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asimorphic.chat.database.dao.ChatDao
import com.asimorphic.chat.database.dao.ChatMessageDao
import com.asimorphic.chat.database.dao.ChatParticipantCrossRefDao
import com.asimorphic.chat.database.dao.ChatParticipantDao
import com.asimorphic.chat.database.entity.ChatEntity
import com.asimorphic.chat.database.entity.ChatMessageEntity
import com.asimorphic.chat.database.entity.ChatParticipantCrossRef
import com.asimorphic.chat.database.entity.ChatParticipantEntity
import com.asimorphic.chat.database.view.LastMessageView

@Database(
    entities = [
        ChatEntity::class,
        ChatParticipantEntity::class,
        ChatParticipantCrossRef::class,
        ChatMessageEntity::class
    ],
    views = [
        LastMessageView::class
    ],
    version = 1
)
abstract class ChirpChatDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatParticipantCrossRefDao: ChatParticipantCrossRefDao
    abstract val chatMessageDao: ChatMessageDao

    companion object {
        const val DB_NAME = "chirp.db"
    }
}