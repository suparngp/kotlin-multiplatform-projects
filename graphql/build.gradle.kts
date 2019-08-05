import constants.Plugins
import constants.ProjectConfig

plugins {
    id("suparnatural-project")
    id("kotlinx-serialization")
}

version = "1.0.5"
suparnatural {
    name = "suparnatural-graphql"
    description = "Graphql type safe models for Kotlin Multiplatform."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/graphql/docs/suparnatural-graphql/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/graphql"
    versionLabel = project.version.toString()
    supportsAndroid = false
    supportsCocoapods = true
    supportsIos = false
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
                implementation("suparnatural-kotlin-multiplatform:concurrency-android:$version")
            }
        }
    }
    androidTest {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:concurrency-android:$version")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:concurrency-metadata:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.11.1")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:concurrency-iosx64:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.11.1")
            }
        }
    }

    iosArm64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:concurrency-iosarm64:$version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.11.1")
            }
        }
    }
}