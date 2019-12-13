import com.android.build.gradle.LibraryExtension
import constants.Plugins
import constants.ProjectConfig

plugins {
    id("suparnatural-project")
    id("kotlinx-serialization")
}

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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.13.0")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-iosx64:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.13.0")
            }
        }
    }

    iosArm64Main {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-iosarm64:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.13.0")
            }
        }
    }

    jvmMain {
        dependencies {
            additional {
                api("suparnatural-kotlin-multiplatform:rx-jvm:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
            }
        }
    }
}

extensions.getByType(LibraryExtension::class).apply {
    packagingOptions {
        pickFirst("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}