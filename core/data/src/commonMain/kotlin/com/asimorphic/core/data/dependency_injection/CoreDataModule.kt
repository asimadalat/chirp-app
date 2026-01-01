package com.asimorphic.core.data.dependency_injection

import com.asimorphic.core.data.auth.KtorAuthService
import com.asimorphic.core.data.log.KermitLogger
import com.asimorphic.core.data.network.HttpClientFactory
import com.asimorphic.core.domain.auth.AuthService
import com.asimorphic.core.domain.log.ChirpLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<ChirpLogger> { KermitLogger }
    single { HttpClientFactory(chirpLogger = get()).create(get()) }
    singleOf(constructor = ::KtorAuthService) bind AuthService::class
}