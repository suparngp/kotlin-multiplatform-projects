import constants.IosTarget
import constants.PluginNames
import constants.SourceSetNames
import constants.TargetNames
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

fun Project.configureCocoapods(kotlin: KotlinMultiplatformExtension, config: CocoapodsConfig) {
    plugins.apply(PluginNames.cocoapods)
    val cocoapods = (kotlin as ExtensionAware).extensions.getByType(CocoapodsExtension::class.java)
    cocoapods.apply {
        summary = config.summary
        homepage = config.homepage
    }
}

fun Project.configureIos(k: KotlinMultiplatformExtension) {
    k.apply {
        iosX64(TargetNames.iosX64)
        iosArm64(TargetNames.iosArm64)

        sourceSets.apply {
            register(SourceSetNames.iosCommonMain) {
                kotlin.srcDir(IosTarget.mainSrcPath)
                resources.srcDir(IosTarget.mainResPath)
            }

            register(SourceSetNames.iosCommonTest) {
                kotlin.srcDir(IosTarget.testSrcPath)
                resources.srcDir(IosTarget.testResPath)
            }

            val iosMain = getByName(SourceSetNames.iosCommonMain)
            val iosTest = getByName(SourceSetNames.iosCommonTest)

            val iosX64Main = getByName(SourceSetNames.iosX64Main) {
                dependsOn(iosMain)
            }

            val iosX64Test = getByName(SourceSetNames.iosX64Test) {
                dependsOn(iosTest)
            }

            val iosArm64Main = getByName(SourceSetNames.iosArm64Main) {
                dependsOn(iosMain)
            }

            val iosArm64Test = getByName(SourceSetNames.iosArm64Test) {
                dependsOn(iosTest)
            }
        }
    }

    val copyPlist = tasks.create("copyPlist", Copy::class.java) {
        val binary = (k.targets.getByName(TargetNames.iosX64) as KotlinNativeTarget).binaries.getTest("DEBUG")
        val infoPlistSrc = file("$rootProject.projectDir/src/iosTest/resources/Info.plist")
        val infoPlistDest = file(binary.outputDirectory)
        from(infoPlistSrc)
        into(infoPlistDest)
    }

    tasks.create("iosTest") {
        val device = findProperty("iosDevice")?.toString() ?: "iPhone 8"
        val target = k.targets.getByName(TargetNames.iosX64) as KotlinNativeTarget
        val testBinary = target.binaries.getTest("DEBUG")
        println(testBinary.linkTaskName)
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

