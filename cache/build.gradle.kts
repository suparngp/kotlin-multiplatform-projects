import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

version = "1.0.5"

suparnatural {
    name = "suparnatural-cache"
    description = "Multiplatform File system api for iOS and Android."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/cache"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
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
                implementation("suparnatural-kotlin-multiplatform:utilities-android:$version")
                implementation("suparnatural-kotlin-multiplatform:concurrency-android:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-android:$version")
            }
        }
    }
    androidTest {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-android:$version")
                implementation("suparnatural-kotlin-multiplatform:concurrency-android:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-android:$version")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-metadata:$version")
                implementation("suparnatural-kotlin-multiplatform:concurrency-metadata:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-metadata:$version")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-iosx64:$version")
                implementation("suparnatural-kotlin-multiplatform:concurrency-iosx64:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-iosx64:$version")
            }
        }
    }
    iosArm64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-iosarm64:$version")
                implementation("suparnatural-kotlin-multiplatform:concurrency-iosarm64:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-iosarm64:$version")
            }
        }
    }
}
