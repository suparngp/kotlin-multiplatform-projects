import constants.IosTarget
import constants.Plugins
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

fun Project.configureCocoapods(config: CocoapodsConfig) {
    plugins.apply(Plugins.cocoapods.id)
    val cocoapods = (kmpKotlin as ExtensionAware).extensions.getByType(CocoapodsExtension::class.java)
    cocoapods.apply {
        summary = config.summary
        homepage = config.homepage
    }
}

fun Project.configureIos() {
    kmpKotlin.apply {
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

            getByName(SourceSetNames.iosX64Main) {
                dependsOn(iosMain)
            }

            getByName(SourceSetNames.iosX64Test) {
                dependsOn(iosTest)
            }

            getByName(SourceSetNames.iosArm64Main) {
                dependsOn(iosMain)
            }

            getByName(SourceSetNames.iosArm64Test) {
                dependsOn(iosTest)
            }
        }
    }

    val copyPlist = tasks.create("copyPlist", Copy::class.java) {
        val binary = (kmpKotlin.targets.getByName(TargetNames.iosX64) as KotlinNativeTarget).binaries.getTest("DEBUG")
        val infoPlistSrc = file("$rootProject.projectDir/src/iosTest/resources/Info.plist")
        val infoPlistDest = file(binary.outputDirectory)
        from(infoPlistSrc)
        into(infoPlistDest)
    }

    tasks.create("iosTest") {
        val device = findProperty("iosDevice")?.toString() ?: "iPhone 8"
        val target = kmpKotlin.targets.getByName(TargetNames.iosX64) as KotlinNativeTarget
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

