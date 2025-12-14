import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.asimorphic.convention.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    implementation(libs.buildkonfig.gradlePlugin)
    implementation(libs.buildkonfig.compiler)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.asimorphic.convention.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidComposeApplication") {
            id = "com.asimorphic.convention.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("cmpApplication") {
            id = "com.asimorphic.convention.cmp.application"
            implementationClass = "CmpApplicationConventionPlugin"
        }

        register("kmpLibrary") {
            id = "com.asimorphic.convention.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }

        register("cmpLibrary") {
            id = "com.asimorphic.convention.cmp.library"
            implementationClass = "CmpLibraryConventionPlugin"
        }

        register("cmpFeature") {
            id = "com.asimorphic.convention.cmp.feature"
            implementationClass = "CmpFeatureConventionPlugin"
        }

        register("buildKonfig") {
            id = "com.asimorphic.convention.buildkonfig"
            implementationClass = "BuildKonfigConventionPlugin"
        }
    }
}