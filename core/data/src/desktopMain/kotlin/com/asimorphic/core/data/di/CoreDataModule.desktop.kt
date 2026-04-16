package com.asimorphic.core.data.di

import com.asimorphic.core.data.auth.createDataStore
import com.asimorphic.core.data.preferences.DataStoreThemePreferences
import com.asimorphic.core.domain.preferences.ThemePreferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { createDataStore() }
    singleOf(constructor = ::DataStoreThemePreferences) bind ThemePreferences::class
}