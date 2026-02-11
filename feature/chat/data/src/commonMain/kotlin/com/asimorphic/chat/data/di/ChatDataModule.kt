package com.asimorphic.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.asimorphic.chat.data.chat.KtorChatParticipantService
import com.asimorphic.chat.data.chat.KtorChatService
import com.asimorphic.chat.data.chat.OfflineFirstChatRepository
import com.asimorphic.chat.database.DatabaseFactory
import com.asimorphic.chat.domain.chat.ChatParticipantService
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.domain.chat.ChatService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformChatDataModule: Module

val chatDataModule = module {
    includes(platformChatDataModule)

    singleOf(constructor = ::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(constructor = ::KtorChatService) bind ChatService::class
    singleOf(constructor = ::OfflineFirstChatRepository) bind ChatRepository::class

    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}