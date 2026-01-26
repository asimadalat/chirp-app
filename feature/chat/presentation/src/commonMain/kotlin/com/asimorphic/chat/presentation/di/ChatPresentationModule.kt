package com.asimorphic.chat.presentation.di

import com.asimorphic.chat.presentation.chat_menu.ChatListViewModel
import com.asimorphic.chat.presentation.chat_menu_detail.ChatListDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    viewModelOf(constructor = ::ChatListViewModel)
    viewModelOf(constructor = ::ChatListDetailViewModel)
}