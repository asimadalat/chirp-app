plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.buildkonfig)
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

                implementation(dependencyNotation = libs.bundles.ktor.common)
                implementation(dependencyNotation = libs.touchlab.kermit)
                implementation(dependencyNotation = libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(dependencyNotation = libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(dependencyNotation = libs.ktor.client.darwin)
            }
        }
    }
}