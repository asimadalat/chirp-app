package com.asimorphic.chat.data.di

import com.asimorphic.chat.data.lifecycle.AppLifecycleObserver
import com.asimorphic.chat.database.DatabaseFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformChatDataModule: Module = module {
    single { DatabaseFactory() }

    singleOf(constructor = ::AppLifecycleObserver)
}