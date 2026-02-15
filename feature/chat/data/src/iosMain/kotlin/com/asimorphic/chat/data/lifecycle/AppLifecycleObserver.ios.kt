package com.asimorphic.chat.data.lifecycle

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationState
import platform.UIKit.UIApplicationWillEnterForegroundNotification
import platform.UIKit.UIApplicationWillResignActiveNotification

actual class AppLifecycleObserver {
    actual val hasForeground: Flow<Boolean> = callbackFlow {
        val currentState = UIApplication.sharedApplication.applicationState
        val isCurrentlyForeground = when (currentState) {
            // App is actively foreground but is in suspended state
            UIApplicationState.UIApplicationStateInactive -> true

            UIApplicationState.UIApplicationStateActive -> true
            else -> false
        }

        send(element = isCurrentlyForeground)

        val notificationCenter = NSNotificationCenter.defaultCenter

        val enterForegroundObserver = notificationCenter.addObserverForName(
            name = UIApplicationWillEnterForegroundNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                trySend(element = true)
            }
        )
        val foregroundObserver = notificationCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                trySend(element = true)
            }
        )
        val backgroundObserver = notificationCenter.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                trySend(element = false)
            }
        )
        val resignActiveObserver = notificationCenter.addObserverForName(
            name = UIApplicationWillResignActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                trySend(element = false)
            }
        )

        awaitClose {
            notificationCenter.removeObserver(observer = enterForegroundObserver)
            notificationCenter.removeObserver(observer = foregroundObserver)
            notificationCenter.removeObserver(observer = backgroundObserver)
            notificationCenter.removeObserver(observer = resignActiveObserver)
        }
    }
}