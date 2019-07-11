import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
        }
    }
    sourceSets {
        val main by getting {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/resources")
        }
        val androidTest by getting {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/resources")
        }
    }
}

dependencies {
    androidTestImplementation("com.android.support.test:runner:1.0.2")
}

kotlin {
    cocoapods {
        // Configure fields required by CocoaPods.
        summary = name
        homepage = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/$name"
    }

    iosX64("iosX64")
    iosArm64("iosArm64")

    android("android") {
        publishLibraryVariants("release")
    }


    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))

            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val iosMain = create("iosMain")
        val iosTest = create("iosTest")

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Test by getting {
            dependsOn(iosTest)
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosX64Test by getting {
            dependsOn(iosTest)
        }
    }
}

val projectName = name

bintray {
    //    user = localProperties.get("BINTRAY_USERNAME")
//    key = localProperties.get("BINTRAY_API_KEY")
    override = true
    publish = true
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        //        repo = localProperties.get("BINTRAY_REPOSITORY")
        name = projectName
        setLicenses("MIT")
        vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/$projectName"
//        version {
//            name = "1.0.3"
//            desc = "1.0.3"
//        }
    })

}

val copyPlist = tasks.create<Copy>("copyPlist") {
    val binary = (kotlin.targets["iosX64"] as KotlinNativeTarget).binaries.getTest("DEBUG")
    val infoPlistSrc = file("$rootProject.projectDir/src/iosTest/resources/Info.plist")
    val infoPlistDest = file(binary.outputDirectory)
    from(infoPlistSrc)
    into(infoPlistDest)
}

tasks.create("iosTest") {
    val device = project.findProperty("iosDevice")?.toString() ?: "iPhone 8"
    dependsOn((kotlin.targets["iosX64"] as KotlinNativeTarget).binaries.getTest("DEBUG").linkTaskName)
    dependsOn(copyPlist)
    group = JavaBasePlugin.VERIFICATION_GROUP
    description = "Runs tests for target ios on an iOS simulator"

    doLast {
        val binary = (kotlin.targets["iosX64"] as KotlinNativeTarget).binaries.getTest("DEBUG").outputFile
        exec {
            commandLine("xcrun", "simctl", "spawn", device, binary.absolutePath)
        }
    }
}