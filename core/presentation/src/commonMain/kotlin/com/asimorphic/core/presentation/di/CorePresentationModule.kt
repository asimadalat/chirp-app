package com.asimorphic.core.presentation.di

import com.asimorphic.core.presentation.util.ScopedStoreRegistryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val corePresentationModule = module {
    viewModelOf(constructor = ::ScopedStoreRegistryViewModel)
}