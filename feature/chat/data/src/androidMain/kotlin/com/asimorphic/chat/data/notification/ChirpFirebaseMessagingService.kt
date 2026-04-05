package com.asimorphic.chat.data.notification

import com.asimorphic.chat.domain.notification.DeviceTokenService
import com.asimorphic.core.domain.auth.SessionService
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ChirpFirebaseMessagingService: FirebaseMessagingService() {

    private val sessionService by inject<SessionService>()
    private val applicationScope by inject<CoroutineScope>()
    private val deviceTokenService by inject<DeviceTokenService>()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        applicationScope.launch {
            val authCredential = sessionService.observeAuthCredential().first()
            authCredential?.let {
                deviceTokenService.registerToken(
                    token = token,
                    platform = "ANDROID"
                )
            }
        }
    }
}