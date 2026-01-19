package com.asimorphic.core.designsystem.component.profile_picture

data class ProfilePictureUi(
    val id: String,
    val initials: String,
    val username: String,
    val imageUrl: String? = null
)
