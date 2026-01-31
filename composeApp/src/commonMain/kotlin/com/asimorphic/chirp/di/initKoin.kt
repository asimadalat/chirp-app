package com.asimorphic.chirp.di

import com.asimorphic.auth.presentation.di.authPresentationModule
import com.asimorphic.chat.data.di.chatDataModule
import com.asimorphic.chat.presentation.di.chatPresentationModule
import com.asimorphic.core.data.di.coreDataModule
import com.asimorphic.core.presentation.di.corePresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            corePresentationModule,
            authPresentationModule,
            chatDataModule,
            chatPresentationModule,
            appModule
        )
    }
}