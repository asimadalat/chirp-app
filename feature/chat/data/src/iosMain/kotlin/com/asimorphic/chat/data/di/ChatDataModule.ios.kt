package com.asimorphic.chat.data.di

import com.asimorphic.chat.database.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformChatDataModule: Module = module {
    single { DatabaseFactory() }
}