package com.asimorphic.chat.data.di

import com.asimorphic.chat.data.chat.KtorChatParticipantService
import com.asimorphic.chat.domain.chat.ChatParticipantService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val chatDataModule = module {
    singleOf(constructor = ::KtorChatParticipantService) bind ChatParticipantService::class
}