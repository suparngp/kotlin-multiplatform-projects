import com.android.build.gradle.LibraryExtension
import constants.Plugins
import constants.ProjectConfig

plugins {
    id("suparnatural-project")
    id("kotlinx-serialization")
}

val serializationVersion = "0.14.0"
version = "1.0.7"
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
            }
        }
    }
    androidRelease {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-android:$version")
            }
        }
    }
    androidDebug {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-android-debug:$version")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-metadata:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-iosx64:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationVersion")
            }
        }
    }

    iosArm64Main {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-iosarm64:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationVersion")
            }
        }
    }

    jvmMain {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-jvm:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
            }
        }
    }
}

extensions.getByType(LibraryExtension::class).apply {
    packagingOptions {
        pickFirst("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}