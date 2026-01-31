package com.asimorphic.chat.data.di

import com.asimorphic.chat.data.chat.KtorChatParticipantService
import com.asimorphic.chat.data.chat.KtorChatService
import com.asimorphic.chat.domain.chat.ChatParticipantService
import com.asimorphic.chat.domain.chat.ChatService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val chatDataModule = module {
    singleOf(constructor = ::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(constructor = ::KtorChatService) bind ChatService::class
}