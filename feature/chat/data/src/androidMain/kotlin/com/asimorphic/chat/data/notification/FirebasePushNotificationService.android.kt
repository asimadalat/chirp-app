package com.asimorphic.chat.data.notification

import com.asimorphic.chat.domain.notification.PushNotificationService
import com.asimorphic.core.domain.log.ChirpLogger
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

actual class FirebasePushNotificationService(
    private val logger: ChirpLogger
) : PushNotificationService {
    actual override fun observeDeviceToken(): Flow<String?> = flow {
        try {
            val fcmToken = Firebase.messaging.token.await()
            logger.info(message = "Initial FCM token received: $fcmToken")
            emit(fcmToken)
        } catch (ex: Exception) {
            currentCoroutineContext().ensureActive()
            emit(null)
            logger.error(
                message = "Failed to get FCM token",
                throwable = ex
            )
        }
    }
}