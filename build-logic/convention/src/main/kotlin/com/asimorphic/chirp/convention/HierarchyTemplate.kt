@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.asimorphic.chirp.convention

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

private val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test
    )

    common {
        withCompilations { true }

        group(name = "mobile") {
            withAndroidTarget()
            group(name = "ios") {
                withIos()
            }
        }

        group(name = "jvmCommon") {
            withAndroidTarget()
            withJvm()
        }

        group(name = "native") {
            withNative()

            group(name = "apple") {
                withApple()

                group(name = "ios") {
                    withIos()
                }
                group(name = "macos") {
                    withMacos()
                }
            }
        }
    }
}

fun KotlinMultiplatformExtension.applyHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}