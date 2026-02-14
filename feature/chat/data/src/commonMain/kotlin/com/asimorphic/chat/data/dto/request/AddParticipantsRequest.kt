package com.asimorphic.chat.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class AddParticipantsRequest(
    val userIds: List<String>
)
