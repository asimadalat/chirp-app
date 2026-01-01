package com.asimorphic.auth.presentation.dependency_injection

import com.asimorphic.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(constructor = ::RegisterViewModel)
}