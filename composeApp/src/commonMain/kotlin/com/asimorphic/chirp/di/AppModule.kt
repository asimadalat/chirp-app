package com.asimorphic.chirp.di

import com.asimorphic.chirp.main.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(constructor = ::MainViewModel)
}