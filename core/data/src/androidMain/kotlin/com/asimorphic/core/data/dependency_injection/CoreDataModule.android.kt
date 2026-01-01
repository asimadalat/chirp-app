package com.asimorphic.core.data.dependency_injection

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single<HttpClientEngine> { OkHttp.create() }
}