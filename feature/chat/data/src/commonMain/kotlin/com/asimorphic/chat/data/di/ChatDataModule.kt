package com.asimorphic.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.asimorphic.chat.data.chat.ChatWebSocketConnectionService
import com.asimorphic.chat.data.chat.KtorChatParticipantService
import com.asimorphic.chat.data.chat.KtorChatService
import com.asimorphic.chat.data.chat.OfflineFirstChatRepository
import com.asimorphic.chat.data.chat_message.KtorChatMessageService
import com.asimorphic.chat.data.chat_message.OfflineFirstChatMessageRepository
import com.asimorphic.chat.data.network.ConnectivityRetryHandler
import com.asimorphic.chat.data.network.KtorWebSocketConnector
import com.asimorphic.chat.database.DatabaseFactory
import com.asimorphic.chat.domain.chat.ChatConnectionService
import com.asimorphic.chat.domain.chat.ChatParticipantService
import com.asimorphic.chat.domain.chat.ChatRepository
import com.asimorphic.chat.domain.chat.ChatService
import com.asimorphic.chat.domain.chat_message.ChatMessageRepository
import com.asimorphic.chat.domain.chat_message.ChatMessageService
import kotlinx.serialization.json.Json
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
    singleOf(constructor = ::KtorChatMessageService) bind ChatMessageService::class
    singleOf(constructor = ::OfflineFirstChatMessageRepository) bind ChatMessageRepository::class
    singleOf(constructor = ::ChatWebSocketConnectionService) bind ChatConnectionService::class
    singleOf(constructor = ::KtorWebSocketConnector)
    singleOf(constructor = ::ConnectivityRetryHandler)

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}