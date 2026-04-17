package com.asimorphic.chat.data.di

import com.asimorphic.chat.data.lifecycle.AppLifecycleObserver
import com.asimorphic.chat.data.network.ConnectivityErrorHandler
import com.asimorphic.chat.data.network.ConnectivityObserver
import com.asimorphic.chat.data.notification.DesktopNotifier
import com.asimorphic.chat.data.notification.FirebasePushNotificationService
import com.asimorphic.chat.database.DatabaseFactory
import com.asimorphic.chat.domain.notification.PushNotificationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformChatDataModule = module {
    singleOf(::DatabaseFactory)
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityErrorHandler)
    singleOf(::ConnectivityObserver)
    singleOf(::DesktopNotifier)
    singleOf(::FirebasePushNotificationService) bind PushNotificationService::class
}