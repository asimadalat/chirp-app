package com.asimorphic.chirp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform