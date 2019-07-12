
//import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

println("Plugins")
plugins {
//    id("com.android.library")
    id("suparnatural-project")
}

println(" After Plugins")

suparnatural {
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
}

//tasks.getting(DokkaTask::class) {
//
//    outputFormat = "html"
//    outputDirectory = "${project.projectDir.path}/docs"
//    impliedPlatforms = mutableListOf("Common") // This will force platform tags for all non-common sources e.g. "JVM"
//    kotlinTasks {
//        // dokka fails to retrieve sources from MPP-tasks so they must be set empty to avoid exception
//        // use sourceRoot instead (see below)
//        emptyList()
//    }
//    sourceRoot {
//        // assuming there is only a single source dir...
//        val kotlin = extensions["kotlin"] as KotlinMultiplatformExtension
//        println(kotlin.sourceSets["commonMain"].kotlin.srcDirs.first().absolutePath)
//        path = kotlin.sourceSets["commonMain"].kotlin.srcDirs.first().absolutePath
//        platforms = listOf("Common")
//    }
//}
//
