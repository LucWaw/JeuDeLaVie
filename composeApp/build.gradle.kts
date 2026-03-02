import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.3.0"
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.androidKmpLibrary)
}

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 26
        namespace = "kmp.project.gameoflife"
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    jvm("desktop")



    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.gif)
        }
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            api(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.material3.window.size.class1)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "kmp.project.gameoflife.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "kmp.project.gameoflife"
            packageVersion = "1.0.1"
            windows {
                shortcut = true
                iconFile.set(project.file("icon.ico"))

            }
        }
    }
}
