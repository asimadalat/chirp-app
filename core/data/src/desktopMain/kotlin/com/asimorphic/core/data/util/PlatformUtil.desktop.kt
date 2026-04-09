package com.asimorphic.core.data.util

actual object PlatformUtil {
    actual fun getOSName(): String = System.getProperty("os.name")
}