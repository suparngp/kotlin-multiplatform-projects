import com.android.build.gradle.LibraryExtension
import constants.Plugins
import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

repositories {
    maven(url="https://dl.bintray.com/badoo/maven")
}

val buildNum = 7
version = "1.0.$buildNum"
suparnatural {
    name = "suparnatural-rx-runtime"
    description = "rx-runtime."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/rx-runtime/docs/suparnatural-rx-runtime/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/rx-runtime"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    supportsJvm = true
    buildNumber = buildNum
    bintray {
        publish = true
        repository = extra[ProjectConfig.Properties.bintrayRepository]!!.toString()
        username = extra[ProjectConfig.Properties.bintrayUsername]!!.toString()
        apiKey = extra[ProjectConfig.Properties.bintrayApiKey]!!.toString()
    }
    androidRelease {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-android:$version")
                implementation("com.badoo.reaktive:reaktive-android:1.1.0")
            }
        }
    }
    androidDebug {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-android-debug:$version")
                implementation("com.badoo.reaktive:reaktive-android-debug:1.1.0")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-metadata:$version")
                implementation("com.badoo.reaktive:reaktive:1.1.0")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-iosx64:$version")
                implementation("com.badoo.reaktive:reaktive-iossim:1.1.0")
            }
        }
    }

    iosArm64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-iosarm64:$version")
                implementation("com.badoo.reaktive:reaktive-ios64:1.1.0")
            }
        }
    }

    jvmMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-jvm:$version")
                implementation("com.badoo.reaktive:reaktive-jvm:1.1.0")
            }
        }
    }
}