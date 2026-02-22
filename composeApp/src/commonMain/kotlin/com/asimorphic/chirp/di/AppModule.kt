package com.asimorphic.chirp.di

import com.asimorphic.chirp.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(constructor = ::MainViewModel)

    single {
        CoroutineScope(context = SupervisorJob() + Dispatchers.Default)
    }
}