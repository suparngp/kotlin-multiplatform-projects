import constants.*
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

data class CocoapodsConfig(val summary: String, val homepage: String)

fun Project.configureCocoapods(config: CocoapodsConfig) {
    plugins.apply(Plugins.cocoapods.id)
    val cocoapods = (kmpKotlin as ExtensionAware).extensions.getByType(CocoapodsExtension::class.java)
    cocoapods.apply {
        summary = config.summary
        homepage = config.homepage
    }
}

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
        val mainTargetName = mainTargetName(name)
        val testTargetName = testTargetName(name)

        sourceSets.apply {
            getByName(mainTargetName) {
                if (dependsOnTarget != null) dependsOn(getByName(mainTargetName(dependsOnTarget)))
            }
            getByName(testTargetName) {
                if (dependsOnTarget != null) dependsOn(getByName(testTargetName(dependsOnTarget)))
            }
        }
    }
}

fun iosTargetSrcPath(sourceSetName: String) = "src/$sourceSetName/kotlin";
fun iosTargetResourcesPath(sourceSetName: String) = "src/$sourceSetName/resources";

fun Project.registerIosSourceSet(name: String) {
    kmpKotlin.apply {
        sourceSets.apply {
            register(name) {
                kotlin.srcDir(iosTargetSrcPath(name))
                resources.srcDir(iosTargetResourcesPath(name))
            }
        }
    }
}

fun Project.configureIos() {
    registerIosSourceSet(mainTargetName(TargetNames.ios))
    registerIosSourceSet(testTargetName(TargetNames.ios))
    configureIosTarget(TargetNames.iosX64, IosPlatform.X64, "ios")
    configureIosTarget(TargetNames.iosArm64, IosPlatform.Arm64, "ios")


    val copyPlist = tasks.create(TaskNames.copyIosTestPlist, Copy::class.java) {
        val binary = (kmpKotlin.targets.getByName(TargetNames.iosX64) as KotlinNativeTarget).binaries.getTest("DEBUG")
        val infoPlistSrc = file("$rootProject.projectDir/src/iosTest/resources/Info.plist")
        val infoPlistDest = file(binary.outputDirectory)
        from(infoPlistSrc)
        into(infoPlistDest)
    }

    tasks.create(TaskNames.iosTest) {
        val device = findProperty("iosDevice")?.toString() ?: "iPhone 8"
        val target = kmpKotlin.targets.getByName(TargetNames.iosX64) as KotlinNativeTarget
        val testBinary = target.binaries.getTest("DEBUG")
        dependsOn(testBinary.linkTaskName)
        dependsOn(copyPlist)
        group = JavaBasePlugin.VERIFICATION_GROUP
        description = "Runs tests for target ios on an iOS simulator"

        doLast {
            val binary = testBinary.outputFile
            exec {
                commandLine("xcrun", "simctl", "spawn", device, binary.absolutePath)
            }
        }
    }
}

