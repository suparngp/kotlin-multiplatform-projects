import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

version = "1.0.4"

suparnatural {
    description = "Multiplatform File system api for iOS and Android."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/cache/com.suparnatural.core.cache/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/cache"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    buildNumber = 4
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
                implementation("suparnatural-kotlin-multiplatform:threading-android:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-android:$version")
            }
        }
    }
    androidTest {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-android:$version")
                implementation("suparnatural-kotlin-multiplatform:threading-android:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-android:$version")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-metadata:$version")
                implementation("suparnatural-kotlin-multiplatform:threading-metadata:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-metadata:$version")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-iosx64:$version")
                implementation("suparnatural-kotlin-multiplatform:threading-iosx64:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-iosx64:$version")
            }
        }
    }
    iosArm64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-iosarm64:$version")
                implementation("suparnatural-kotlin-multiplatform:threading-iosarm64:$version")
                implementation("suparnatural-kotlin-multiplatform:fs-iosarm64:$version")
            }
        }
    }
}