import com.android.build.gradle.LibraryExtension
import constants.*
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureAndroid(kotlin: KotlinMultiplatformExtension) {
    plugins.apply(PluginNames.android)
    extensions.getByType(LibraryExtension::class.java).apply {
        compileSdkVersion(AndroidTarget.compileSdkVersion)
        defaultConfig {
            minSdkVersion(AndroidTarget.minSdkVersion)
            targetSdkVersion(AndroidTarget.targetSdkVersion)
            versionCode = ProjectConfig.buildNumber
            versionName = ProjectConfig.versionString
            testInstrumentationRunner = AndroidTarget.testRunnerName
        }
        buildTypes {
            val release by getting {
                isMinifyEnabled = false
            }
        }

        sourceSets {
            val main by getting {
                manifest.srcFile(AndroidTarget.manifestSrcPath)
                java.srcDirs(AndroidTarget.mainSrcPath)
                res.srcDirs(AndroidTarget.mainResPath)
            }
            val androidTest by getting {
                java.srcDirs(AndroidTarget.testSrcPath)
                res.srcDirs(AndroidTarget.testResPath)
            }
        }
    }
    dependencies.add(AndroidTarget.testRunnerConfigurationName, AndroidTarget.testRunnerDependency)

    kotlin.apply {
        android(TargetNames.android) {
            publishLibraryVariants(AndroidTarget.publishVariantRelease)
        }
        sourceSets.apply {
            val androidMain by getting {
//                dependencies {
//                    add(TargetDependencies.Android.main)
//                }
            }
            val androidTest by getting {
                dependencies {
                    add(TargetDependencies.Android.test)
                }
            }
        }
    }
}