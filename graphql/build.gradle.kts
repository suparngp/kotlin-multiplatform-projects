import constants.Plugins
import constants.ProjectConfig

plugins {
    id("suparnatural-project")
    id("kotlinx-serialization")
}

repositories {
    maven(url="https://dl.bintray.com/badoo/maven")
}

version = "1.0.5"
suparnatural {
    name = "suparnatural-graphql"
    description = "Graphql type safe models for Kotlin Multiplatform."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/graphql/docs/suparnatural-graphql/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/graphql"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    supportsJvm = true
    buildNumber = 5
    bintray {
        publish = true
        repository = extra[ProjectConfig.Properties.bintrayRepository]!!.toString()
        username = extra[ProjectConfig.Properties.bintrayUsername]!!.toString()
        apiKey = extra[ProjectConfig.Properties.bintrayApiKey]!!.toString()
    }
    androidMain {
        dependencies {
            additional {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
            }
        }
    }
    androidTest {
        dependencies {
            additional {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
            }
        }
    }
    androidRelease {
        dependencies {
            additional {
                implementation("com.badoo.reaktive:reaktive-android:1.1.0")
            }
        }
    }
    androidDebug {
        dependencies {
            additional {
                implementation("com.badoo.reaktive:reaktive-android-debug:1.1.0")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                implementation("com.badoo.reaktive:reaktive:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.13.0")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                implementation("com.badoo.reaktive:reaktive-iossim:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.13.0")
            }
        }
    }

    iosArm64Main {
        dependencies {
            additional {
                implementation("com.badoo.reaktive:reaktive-ios64:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.13.0")
            }
        }
    }

    jvmMain {
        dependencies {
            additional {
                implementation("com.badoo.reaktive:reaktive-jvm:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
            }
        }
    }
    jvmTest {
        dependencies {
            additional {
                implementation("com.badoo.reaktive:reaktive-jvm:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
            }
        }
    }
}