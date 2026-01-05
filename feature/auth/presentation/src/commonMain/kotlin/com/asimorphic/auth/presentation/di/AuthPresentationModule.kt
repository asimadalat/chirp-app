package com.asimorphic.auth.presentation.di

import com.asimorphic.auth.presentation.email_verification.EmailVerificationViewModel
import com.asimorphic.auth.presentation.login.LoginViewModel
import com.asimorphic.auth.presentation.register.RegisterViewModel
import com.asimorphic.auth.presentation.register_success.RegisterSuccessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(constructor = ::RegisterViewModel)
    viewModelOf(constructor = ::RegisterSuccessViewModel)
    viewModelOf(constructor = ::EmailVerificationViewModel)
    viewModelOf(constructor = ::LoginViewModel)
}