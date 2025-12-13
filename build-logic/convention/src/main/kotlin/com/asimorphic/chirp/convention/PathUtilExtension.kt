package com.asimorphic.chirp.convention

import org.gradle.api.Project
import java.util.Locale

fun Project.pathToPackageName(): String {
    val relativePackageName: String = path
        .replace(oldChar = ':', newChar = '.')
        .lowercase()

    return "com.asimorphic$relativePackageName"
}

fun Project.pathToResourcePrefix(): String {
    return path
        .replace(oldChar = ':', newChar = '_')
        .lowercase()
        .drop(n = 1) + "_"
}

fun Project.pathToFrameworkName(): String {
    val parts: List<String> = this.path
        .split(":", "-", "_", " ")

    return parts.joinToString("") { part ->
        part.replaceFirstChar {
            it.titlecase(Locale.ROOT)
        }
    }
}