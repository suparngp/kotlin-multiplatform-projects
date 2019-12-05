import com.android.build.gradle.LibraryExtension
import constants.*
import extensions.SuparnaturalPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting

fun Project.configureAndroid(config: SuparnaturalPluginExtension) {
    plugins.apply(Plugins.android.id)
    extensions.getByType(LibraryExtension::class.java).apply {
        buildToolsVersion = "29.0.2"
        compileSdkVersion(AndroidTarget.compileSdkVersion)
        defaultConfig {
            minSdkVersion(AndroidTarget.minSdkVersion)
            targetSdkVersion(AndroidTarget.targetSdkVersion)
            versionCode = config.buildNumber
            versionName = config.versionLabel
            testInstrumentationRunner = AndroidTarget.testRunnerName
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
            }
        }

        sourceSets {
            getByName("main") {
                manifest.srcFile(AndroidTarget.manifestSrcPath)
                java.srcDirs(AndroidTarget.mainSrcPath)
                res.srcDirs(AndroidTarget.mainResPath)
            }

            getByName("androidTest") {
                java.srcDirs(AndroidTarget.testSrcPath)
                res.srcDirs(AndroidTarget.testResPath)
            }
        }
    }
    dependencies.add(AndroidTarget.testRunnerConfigurationName, AndroidTarget.testRunnerDependency)

    kmpKotlin.apply {
        android(TargetNames.android) {
            publishLibraryVariants(AndroidTarget.publishVariantRelease)
        }
    }
    configureKmpSourceSet(SourceSetNames.androidMain, config.androidMain)
    configureKmpSourceSet(SourceSetNames.androidTest, config.androidTest)

    // variant specific configurations
    configureKmpSourceSet(SourceSetNames.androidAndroidTest, config.androidAndroidTest)
    configureKmpSourceSet(SourceSetNames.androidAndroidTestDebug, config.androidAndroidTestDebug)
    configureKmpSourceSet(SourceSetNames.androidDebug, config.androidDebug)
    configureKmpSourceSet(SourceSetNames.androidRelease, config.androidRelease)
    configureKmpSourceSet(SourceSetNames.androidTestDebug, config.androidTestDebug)
    configureKmpSourceSet(SourceSetNames.androidTestRelease, config.androidTestRelease)

}