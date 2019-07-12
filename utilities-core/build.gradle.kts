
//import org.jetbrains.dokka.gradle.DokkaTask
import constants.PluginNames
import constants.SourceSetNames
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

println("Plugins")
plugins {
//    id("com.android.library")
    id("suparnatural-project")
    id("org.jetbrains.dokka")
}

println(" After Plugins")

suparnatural {
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
}