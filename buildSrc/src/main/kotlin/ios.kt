import constants.*
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Copy

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
        val mainTargetName = sourceSetName(name, SourceSetType.Main)
        val testTargetName = sourceSetName(name, SourceSetType.Test)

        sourceSets.apply {
            getByName(mainTargetName) {
                if (dependsOnTarget != null) dependsOn(getByName(sourceSetName(dependsOnTarget, SourceSetType.Main)))
            }
            getByName(testTargetName) {
                if (dependsOnTarget != null) dependsOn(getByName(sourceSetName(dependsOnTarget, SourceSetType.Test)))
            }
        }
    }
}



fun Project.configureIosTestTask() {
    tasks.create(TaskNames.iosTest) {
        group = JavaBasePlugin.VERIFICATION_GROUP
        description = "Runs tests for target ios on an iOS simulator"
        val copyIosTestPlistTask = tasks.getByName(TaskNames.copyIosTestPlist)
        dependsOn(copyIosTestPlistTask)

        val targetName = getTargetName()
        if (targetName != null) {
            val target = getNativeTarget(targetName)

            val testBinary = target.binaries.getTest("DEBUG")
            dependsOn(testBinary.linkTaskName)

            doLast {
                val device = findProperty(ProjectConfig.Properties.iosDevice)?.toString() ?: ProjectConfig.defaultIosSimulator
                val binary = testBinary.outputFile
                exec {
                    commandLine("xcrun", "simctl", "spawn", device, binary.absolutePath)
                }
            }
        }
    }
}

fun Project.configureCopyPlistTask() {
    tasks.create(TaskNames.copyIosTestPlist, Copy::class.java) {
        doLast {
            val targetName = getTargetName() ?: throw GradleException("Missing Target Name")
            val testSourceSetName = sourceSetName(targetName, SourceSetType.Test)
            val binary = getNativeTarget(targetName).binaries.getTest("DEBUG")
            val infoPlistSrc = file("$rootProject.projectDir/${resourcesPath(testSourceSetName)}/Info.plist")
            val infoPlistDest = file(binary.outputDirectory)
            from(infoPlistSrc)
            into(infoPlistDest)
        }
    }
}

fun Project.configureIos(release: Boolean = false) {

    if (release) {
        registerSourceSet(sourceSetName(TargetNames.ios, SourceSetType.Main))
        registerSourceSet(sourceSetName(TargetNames.ios, SourceSetType.Test))
        configureIosTarget(TargetNames.iosX64, IosPlatform.X64, TargetNames.ios)
        configureIosTarget(TargetNames.iosArm64, IosPlatform.Arm64, TargetNames.ios)
    } else {
        val platform = if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) IosPlatform.Arm64 else IosPlatform.X64
        configureIosTarget(TargetNames.ios, platform)
    }
//    registerSourceSet(sourceSetName(TargetNames.ios, SourceSetType.Main))
//    registerSourceSet(sourceSetName(TargetNames.ios, SourceSetType.Test))
//    configureIosTarget(TargetNames.iosX64, IosPlatform.X64, TargetNames.ios)
//    configureIosTarget(TargetNames.iosArm64, IosPlatform.Arm64, TargetNames.ios)

    configureCopyPlistTask()
    configureIosTestTask()
}

