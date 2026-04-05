package com.asimorphic.chirp

import com.asimorphic.chat.data.notification.IosDeviceTokenHolder

object IosDeviceTokenHolderBridge {
    fun updateToken(token: String) = IosDeviceTokenHolder.updateToken(token)
}