package com.asimorphic.chirp.di

import com.asimorphic.chirp.application.ApplicationViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val desktopModule = module {
    singleOf(constructor = ::ApplicationViewModel)
}