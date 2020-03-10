import constants.IosTarget
import constants.ProjectConfig
import constants.TargetNames
import constants.TaskNames
import extensions.SourceSetExtension
import extensions.SuparnaturalPluginExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Copy
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation

enum class IosPlatform {
    X64,
    Arm64
}


fun Project.configureIosTarget(name: String, platform: IosPlatform, dependsOnTarget: String? = null) {
    kmpKotlin.apply {
        when (platform) {
            IosPlatform.X64 -> iosX64(name)
            IosPlatform.Arm64 -> iosArm64(name)
        }
        val mainSourceSetName = sourceSetName(name, SourceSetType.Main)
        val testSourceSetName = sourceSetName(name, SourceSetType.Test)

        sourceSets.apply {
            getByName(mainSourceSetName) {
                if (dependsOnTarget != null) dependsOn(getByName(sourceSetName(dependsOnTarget, SourceSetType.Main)))
            }
            getByName(testSourceSetName) {
                if (dependsOnTarget != null) dependsOn(getByName(sourceSetName(dependsOnTarget, SourceSetType.Test)))
            }
        }
    }
}

// enable generics support for ios
fun Project.configureIosGenerics() {
    kmpKotlin.apply {
        val allIosTargetNames = listOf(TargetNames.ios, TargetNames.iosX64, TargetNames.iosArm64)
        targets.filter { allIosTargetNames.contains(it.name) }
                .forEach { target ->
                    target.compilations.forEach { compilation ->
                        (compilation as KotlinNativeCompilation).kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
                    }
                }
    }
}

fun Project.configureIosTestTask(testTargetName: String, srcIosTargetName: String, testTaskName: String) {
    tasks.getByName(testTaskName).apply {
        doFirst {
            copy {
                val testSourceSetName = sourceSetName(srcIosTargetName, SourceSetType.Test)
                val binary = getNativeTarget(testTargetName).binaries.getTest("DEBUG")
                val infoPlistSrc = file("${project.projectDir}/${resourcesPath(testSourceSetName)}/Info.plist")
                val infoPlistDest = file(binary.outputDirectory)
                println("MARK:// infoPlistSrc: $infoPlistSrc \n infoPlistDest:$infoPlistDest")
                from(infoPlistSrc)
                into(infoPlistDest)
            }
        }
    }
}

fun Project.configureIos(config: SuparnaturalPluginExtension, release: Boolean = false) {

    val sourceSets = mutableListOf<Pair<String, SourceSetExtension<*>>>()

    if (release) {
        registerSourceSet(sourceSetName(TargetNames.ios, SourceSetType.Main))
        registerSourceSet(sourceSetName(TargetNames.ios, SourceSetType.Test))
        configureIosTarget(TargetNames.iosX64, IosPlatform.X64, TargetNames.ios)
        configureIosTarget(TargetNames.iosArm64, IosPlatform.Arm64, TargetNames.ios)
        sourceSets.add(Pair(sourceSetName(TargetNames.iosX64, SourceSetType.Main), config.iosX64Main))
        sourceSets.add(Pair(sourceSetName(TargetNames.iosX64, SourceSetType.Test), config.iosX64Test))
        sourceSets.add(Pair(sourceSetName(TargetNames.iosArm64, SourceSetType.Main), config.iosArm64Main))
        sourceSets.add(Pair(sourceSetName(TargetNames.iosArm64, SourceSetType.Test), config.iosArm64Test))
    } else {
        val platform = if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) IosPlatform.Arm64 else IosPlatform.X64
        configureIosTarget(TargetNames.ios, platform)
        sourceSets.add(Pair(sourceSetName(TargetNames.ios, SourceSetType.Main), if (platform == IosPlatform.X64) config.iosX64Main else config.iosArm64Main))
        sourceSets.add(Pair(sourceSetName(TargetNames.ios, SourceSetType.Test), if (platform == IosPlatform.X64) config.iosX64Test else config.iosArm64Test))
    }

    // turn on generics
    configureIosGenerics()

    sourceSets.forEach {
        configureKmpSourceSet(it.first, it.second)
    }

    val testTargetName = if (release) TargetNames.iosX64 else TargetNames.ios
    val sourceSetName = if (release) sourceSetName(TargetNames.iosX64, SourceSetType.Test) else sourceSetName(TargetNames.ios, SourceSetType.Test)
    configureIosTestTask(testTargetName, sourceSetName, if (release) TaskNames.iosX64Test else TaskNames.iosTest)
}

