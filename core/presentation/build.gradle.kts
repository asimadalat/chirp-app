plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(projects.core.domain)

                implementation(compose.components.resources)

                implementation(dependencyNotation = libs.material3.adaptive)
                implementation(dependencyNotation = libs.bundles.koin.common)
                implementation(dependencyNotation = libs.jetbrains.lifecycle.compose)
            }
        }

        val mobileMain by creating {
            dependencies {
                implementation(dependencyNotation = libs.moko.permissions)
                implementation(dependencyNotation = libs.moko.permissions.compose)
                implementation(dependencyNotation = libs.moko.permissions.notifications)
            }
            dependsOn(commonMain.get())
        }
        androidMain.get().dependsOn(mobileMain)

        val iosMain by creating {
            dependsOn(mobileMain)
        }

        listOf(
            iosArm64(),
            iosX64(),
            iosSimulatorArm64()
        ).forEach { target ->
            getByName("${target.name}Main") {
                dependsOn(iosMain)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}