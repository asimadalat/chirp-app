package com.asimorphic.chirp.dependency_injection

import com.asimorphic.auth.presentation.dependency_injection.authPresentationModule
import com.asimorphic.core.data.dependency_injection.coreDataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(coreDataModule, authPresentationModule)
    }
}